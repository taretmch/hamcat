# 目次

<!-- vim-markdown-toc GFM -->

- [目次](#目次)
- [4. Kleisli 圏](#4-kleisli-圏)
  - [4.1 ロギング関数の合成](#41-ロギング関数の合成)
  - [4.2 Writer 圏](#42-writer-圏)
    - [4.2.1 Writer 圏の対象と射](#421-writer-圏の対象と射)
    - [4.2.2 Writer 圏における射の合成](#422-writer-圏における射の合成)
    - [4.2.3 Writer 圏は圏の公理を満たすか](#423-writer-圏は圏の公理を満たすか)
  - [4.3 Kleisli 圏](#43-kleisli-圏)
  - [4.4 Cats における Writer と Kleisli](#44-cats-における-writer-と-kleisli)

<!-- vim-markdown-toc -->

# 4. Kleisli 圏

ここまでで、型と純粋関数を圏としてモデル化する方法をみてきました。その際に計算効果を持つ非純粋な関数をモデル化するための概念として、モナドが出てきましたね。

ここでは、計算効果についてイメージを深めるため、計算効果の例をモナドの概念を出さない程度に考えていきます。

## 4.1 ロギング関数の合成

計算効果の例として、プログラムの実行をロギングする関数について考えます。

例えば、論理値の否定を返す関数 `negate` の実行と、整数が偶数であるかを判別する関数 `isEven` の実行のログをとりたいとします。 `negate` の処理自体は論理値を論理値に変換するものなので、型は `Boolean => Boolean` になりそうです。しかし、ここではログも一緒に出力したいので、返り値の型を `(String, Boolean)` として定義してみます。 `isEven` 関数も同様です。

```scala
def negate: Boolean => (String, Boolean) = n => ("negate ", !n)
def isEven: Int => (String, Boolean) = n => ("isEven ", n % 2 == 0)
```

ここで、 `negate` と `isEven` を合成して、整数が奇数であるかを判別する関数 `isOdd` を作ることを考えます。2つの関数 `f: A => B` と `g: B => C` を合成するには、一方の出力の型、つまり `f` の出力 `B`、ともう一方の入力の型、つまり `g` の入力 `B` が一致する必要がありました。ところが、 `negate` と `isEven` は型が一致せず、通常の方法では合成できません。代わりに、以下のように定義するとうまくいきそうです：

```scala
def isOdd: Int => (String, Boolean) = n => {
  val (log1, res1) = isEven(n)
  val (log2, res2) = negate(res1)
  (log1 + log2, res2)
}
```

実は、今考えたような構造は Writer 圏と呼ばれます。次の項では Writer 圏を形式化していきます。

## 4.2 Writer 圏

先ほど見たロギングの合成の例を圏論の言葉に落とし込むと、Writer 圏というものを考えることができます。

### 4.2.1 Writer 圏の対象と射

Writer 圏では、次のようなデータ型 `Writer` を導入します。

```scala
type Writer[A] = (String, A)
```

すなわち、型 `Writer[A]` はログを表現する型 `String` と計算の出力 `A` のタプルです。

この圏では、対象として型を採用し、対象 `A` から `B` への射として以下のようにラップされた関数を採用します：

```scala
f: A => Writer[B]
```

2つの関数 `negate: Boolean => (String, Boolean)` と `isEven: Int => (String, Boolean)` は、以下のように書き換えれば Writer 圏における射とみなせます。

```scala
def negate: Boolean => Writer[Boolean] = n => ("negate ", !n)
def isEven: Int => Writer[Boolean] = n => ("isEven ", n % 2 == 0)
```

### 4.2.2 Writer 圏における射の合成

Writer 圏における射の合成は、 `isOdd` 関数の定義を抽象化したものと考えることができます。関数 `f: A => Writer[B]`、関数 `g: B => Writer[C]` を合成して関数 `f >=> g: A => Writer[C]` を定義してみましょう。

```scala
implicit class WriterOps[A, B](f: A => Writer[B]) {
  def >=>[C](g: B => Writer[C]): A => Writer[C] =
    a => {
      val (log1, b) = f(a)
      val (log2, c) = g(b)
      (log1 + log2, c)
    }
}
```

`>=>` 演算は fish 演算子と呼ばれるもので、引数で受け取った2つの関数を合成した関数を返します。

これで、Writer 圏における関数合成は定義できました！

### 4.2.3 Writer 圏は圏の公理を満たすか

ここで一度、圏が満たすべき性質に立ち戻ってみましょう。1つ目は関数の合成が結合律を満たすこと、2つ目は任意の対象に対して恒等射が存在することでした。

この合成 `>=>` は、結合律を満たすでしょうか？

タプルの各要素に分解して考えてみましょう。1つ目の要素の合成は、 `String` の連結です。これは自明に結合律を満たしますね。

```scala
// str1, str2, str3 はいずれも String 型とする
str1 ++ str2 ++ str3 == (str1 ++ str2) ++ str3 == str1 ++ (str2 ++ str3)
```

2つ目の要素の合成は、標準的な関数合成です。これも結合律を満たしますね。

```scala
h compose g compose f == (h compose g) compose f == h compose (g compose f)
```

`>=>` の内部では文字列の連結と標準的な関数合成をしているだけなので、`>=>` は結合律を満たします！

次に、恒等射の存在について考えてみましょう。これまでの議論に恒等射は出ていないので、新しく定義します。

Writer 圏における恒等射の性質として、1つ目の要素であるログ文字列をそのまま返し、2つ目の要素である計算結果もそのまま返す必要があります。すなわち、1つ目の要素に空文字列を渡し、2つ目の要素には引数と同じ値を渡せば良いです。したがって、恒等射 `pure` は以下のように定義できます。

```scala
def pure[A](a: A): Writer[A] = ("", a)
```

## 4.3 Kleisli 圏

## 4.4 Cats における Writer と Kleisli
