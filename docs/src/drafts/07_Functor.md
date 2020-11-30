# 7. 関手

圏は対象の集まりと射の集まりからなりますが、これまでに何度か「対象を圏として圏を構成できるのではないか？」と思った人もいるのではないでしょうか。

その疑問に対する答えは「できる」です。今回定義する関手を使えば、圏の構造（つまり、圏の図式の形）を維持したまま別の圏に変換できるようになります。

関手は、非常に単純ですが強力な概念です。本章では、関手とは何かについて定義し、プログラミングにおける関手の例を示します。

## 7.1 関手とは

関手 (functor) は、ある圏 C を別のある圏 D に変換する対応 F のことです。

関手の例としては Option 関手、List 関手、Writer 関手、モノイド準同型などがあります。モノイド準同型は、モノイド間の関手です。

Option 関手は、型 `A` の値を `Option` で包んで型 `Option[A]` に変換します。また、関数 `A => B` を関数 `Option[A] => Option[B]` に変換します。

同様に、List 関手は型 `A` を `List[A]` に変換し、関数 `A => B` を `List[A] => List[B]` に変換します。Writer 関手も型 `A` を `Writer[A]` に変換し、関数 `A => B` を `Writer[A] => Writer[B]` に変換します。

圏は対象と射から構成されるので、圏を変換するには対象と射それぞれに関する対応を定義する必要があります。

<div align="center">

![関手](./images/07_functor.png)

</div>

### 7.1.1 対象関数

関手において、ある圏の対象を別のある圏の対象に変換するような対応を対象関数といいます。一般に、圏 C から D への関手 F は、圏 C の対象 A を D の対象 F A に対応させます。

Option 関手の例で言うと、Option 関手は型 `A` を型 `Option[A]` に対応させています。

```scala mdoc
def objOptFunc[A]: A =>  Option[A] = Option(_)

objOptFunc(3)
objOptFunc("Hoge")
```

### 7.1.2 射関数

関手において、ある圏の射を別のある圏の射に変換するような対応を射関数といいます。一般に、圏 C から D への関手 F は、圏 C の射 f: A -> B を D の射 F(f): F(A) -> F(B) に対応させます。

Option 関手の例で言うと、射 `f: A => B` を `F(f): Option[A] => Option[B]` に対応させる必要があります。この対応は、標準ライブラリにある `Option#map` メソッドによって実現されます：

```scala mdoc
def isEven: Int => Boolean = n => n % 2 == 0

Option(3).map(isEven)
```

<div align="center">

![Option 関手](./images/07_functor.png)

</div>

この射関数が満たすべき性質として、以下の2つがあります：

1. C の射 f, g の合成 g . f について F(f . g) = F(f) . F(g) が成り立つこと。
2. C の任意の対象 A の恒等射 idA について F(idA) = idFA が成り立つこと。ただし、idFA は圏 D の対象 F(A) についての恒等射です。

1つ目の性質は、関手が射の合成を保存することを意味します。

```scala mdoc
def negate: Boolean => Boolean = b => !b

// F(f . g)
Option((negate compose isEven) (3))

// F(f) . F(g)
Option(3).map(isEven).map(negate)
```

<div align="center">

![合成の保存](./images/07_functor_composition.png)

</div>

2つ目の性質は、関手が恒等射を保存することを意味します。

```scala mdoc
def identity[A]: A => A = a => a

// F(idA)
Option(identity(3))

// idFA
identity(Option(3))
```

以上の性質は圏の構造を保存する対応を表す性質です。このような2つの性質を**関手性** (functor laws) と呼びます。

### 7.1.3 関手の定義

では、関手の定義を与えましょう。一般に、関手は以下のように定義されます。

---

圏 C から圏 D への**関手** (functor) F とは、以下を満たす対応のことです。

- C の射 f: A -> B を D の射 F(f): F(A) -> F(B) に対応させること。
- C の射 f, g の合成 f . g について F(f . g) = F(f) . F(g) が成り立つこと。
- C の任意の対象 A の恒等射 idA について F(idA) = idFA が成り立つこと。ただし、idFA は D の対象 F(A) についての恒等射です。

---

先ほどみたように、2 番目と 3 番目は関手性を表します。

なお、圏 C と D は同じであってもよく、特に圏 C から圏 C への関手は自己関手 (endofunctor) と呼ばれます。Scala 圏における関手は全て、自己関手です。

## 7.2 プログラミングにおける関手

前節では、関手の定義を与えました。本節では、Scala プログラミングにおける関手を考えていきます。

### 7.2.1 Functor 型クラス

関手は Scala において、以下のような型クラス [Functor](https://github.com/taretmch/scala-category-training/blob/master/src/main/scala/data/Functor.scala) として実装できます。

```scala
trait Functor[F[_]] {
  def fmap[A, B](fa: F[A])(f: A => B): F[B]
}
```

`F[_]` は型構築子で、なんらかの型を与えることによって具象型になります。`Functor` トレイトは型構築子 `F[_]` を型パラメータとして持っており、射関数 `fmap: F[A] => (A => B) => F[B]` を持ちます。

`fmap` メソッドは関数を引き上げる (lift)、とも言われます。関数 `A => B` は `fmap` によって `F[_]` 上の関数 `F[A] => F[B]` に引き上げられます。

### 7.2.2 Option 関手

`Functor` のインスタンスは、implicit value や継承によって生成することができます。ここでは、`Option` についての `Functor` のインスタンスを定義してみましょう。

Option 関手のインスタンスは、以下のように実装できます。

```scala
implicit val OptionFunctor: Functor[Option] = new Functor[Option] {
  def fmap[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
    case None    => None
    case Some(a) => Some(f(a))
  }
}
```

Option 関手の `fmap` メソッドは `Option#map` メソッドと同じです。実装を見てわかる通り、`fmap` メソッドが関手性を満たすかどうか、つまり圏の構造を維持する対応かどうかは実装によります。定義だけでは `fmap` メソッドが必ず関手性を満たすとは言えませんが、関手性を満たすように `fmap` メソッドを実装しなければいけません。

実際にこのインスタンスを使ってみましょう。`category.Implicits._` をインポートすれば、インスタンスが使えるようになります。`fmap` に `Option(3)` と `isEven` を与えると、`Option(3)` の中の値に `isEven` を適用した結果が出力されます。

```scala mdoc
import category.Implicits._

OptionFunctor.fmap(Option(3))(isEven)
```

なお、毎回 `OptionFunctor.fmap(...)` と書くのは面倒ですし、不便です。この場合、以下のようにシンタックスを定義することによって `Option#fmap` メソッドとして呼び出せるようになります。

```scala
implicit class FunctorOps[F[_], A](v: F[A])(implicit functor: Functor[F]) {
  def fmap[B](f: A => B): F[B] = functor.fmap[A, B](v)(f)
}
```

```scala mdoc
Option(3).fmap(isEven)
```

では、この Option 関手の `fmap` メソッドが関手性を満たすかどうかについて調べてみましょう。

関手性とは、以下が成り立つことでした。

- C の射 f, g の合成 f . g について F(f . g) = F(f) . F(g) が成り立つこと。
- C の任意の対象 A の恒等射 idA について F(idA) = idFA が成り立つこと。ただし、idFA は D の対象 F(A) についての恒等射です。

1つ目について、以下のテストコードで検証します。

```scala
val increment:   Int => Int     = n => n + 1
val isEven:      Int => Boolean = n => n % 2 == 0
def identity[A]: A   => A       = a => a

val none: Option[Int] = None
"Option functor" should "射の合成を保存する" in {
  // F(f . g) == F(g) . F(f)
  // Case: Some(1)
  assert(Option(1).fmap(isEven compose increment) == Option(1).fmap(increment).fmap(isEven))

  // Case: None
  assert(none.fmap(isEven compose increment) == none.fmap(increment).fmap(isEven))
}
```

2つ目については、以下のテストコードで検証します。

```scala
it should "恒等射を恒等射へ写す" in {
  // F(idA) == idFA
  // Case: Some(1)
  assert(Option(1).fmap(identity) == identity(Option(1)))

  // Case: None
  assert(none.fmap(identity) == identity(none))
}
```

テストを実行してみると、成功しました！ここで実装した `fmap` は関手性を満たしてそうです。

```sh
sbt:scala-category-training> testOnly category.data.FunctorSpec
[info] FunctorSpec:
[info] Option functor
[info] - should 射の合成を保存する
[info] - should 恒等射を恒等射へ写す
...
[info] All tests passed.
```

### 7.2.4 Reader 関手

