<!-- omit in toc -->
# 7. 関手

圏は対象の集まりと射の集まりからなりますが、これまでに何度か「対象を圏として圏を構成できるのではないか？」と思った人もいるのではないでしょうか。

その疑問に対する答えは「できる」です。今回定義する関手を使えば、圏の構造（つまり、圏の図式の形）を維持したまま別の圏に変換できるようになります。

関手は、非常に単純ですが強力な概念です。本章では、関手とは何かについて定義し、プログラミングにおける関手の例を示します。

<!-- omit in toc -->
# 目次

- [7.1 関手とは](#71-関手とは)
  - [7.1.1 対象関数](#711-対象関数)
  - [7.1.2 射関数](#712-射関数)
  - [7.1.3 関手の定義](#713-関手の定義)
- [7.2 プログラミングにおける関手](#72-プログラミングにおける関手)
  - [7.2.1 Functor 型クラス](#721-functor-型クラス)
  - [7.2.2 Option 関手](#722-option-関手)
  - [7.2.3 Reader 関手](#723-reader-関手)
- [7.3 関手の合成](#73-関手の合成)
- [まとめ](#まとめ)

## 7.1 関手とは

**関手** (functor) は、ある圏 **C** を別のある圏 **D** に変換する対応 F のことです。

関手の例としては Option 関手、List 関手、Writer 関手、モノイド準同型などがあります。モノイド準同型は、モノイド間の関手です。

Option 関手は、型 `A` の値を `Option` で包んで型 `Option[A]` に変換します。また、関数 `A => B` を関数 `Option[A] => Option[B]` に変換します。

同様に、List 関手は型 `A` を `List[A]` に変換し、関数 `A => B` を `List[A] => List[B]` に変換します。Writer 関手も型 `A` を `Writer[A]` に変換し、関数 `A => B` を `Writer[A] => Writer[B]` に変換します。

圏は対象と射から構成されるので、圏を変換するには対象と射それぞれに関する対応を定義する必要があります。

<div align="center">

![関手](./images/07_functor.png)

</div>

### 7.1.1 対象関数

関手において、ある圏の対象を別のある圏の対象に変換するような対応を対象関数といいます。一般に、圏 **C** から **D** への関手 F は、圏 **C** の対象 a を **D** の対象 F(a) に対応させます。

Option 関手の例で言うと、Option 関手は型 `A` を型 `Option[A]` に対応させています。

```scala
def objOptFunc[A]: A =>  Option[A] = Option(_)

objOptFunc(3)
// res0: Option[Int] = Some(value = 3)
objOptFunc("Hoge")
// res1: Option[String] = Some(value = "Hoge")
```

### 7.1.2 射関数

関手において、ある圏の射を別のある圏の射に変換するような対応を射関数といいます。一般に、圏 **C** から **D** への関手 F の射関数は、圏 **C** の射 f: a -> b を **D** の射 F(f): F(a) -> F(b) に対応させます。Hamcat において、射関数は `fmap` メソッドとして定義しています。

Option 関手は、例で言うと、射 `f: A => B` を `fmap(f): Option[A] => Option[B]` に対応させる必要があります。この対応は、標準ライブラリにある `Option#map` メソッドによって実現されます：

```scala
def isEven: Int => Boolean = n => n % 2 == 0

Option(3).map(isEven)
// res2: Option[Boolean] = Some(value = false)
```

<div align="center">

![Option 関手](./images/07_option_functor.png)

</div>

この射関数が満たすべき性質として、以下の2つがあります：

1. **C** の射 f, g の合成 `g compose f` について `fmap(g compose f) == fmap(g) compose fmap(f)` が成り立つこと。
2. C の任意の対象 A の恒等射 `identity[A]` について `fmap(identity[A]) === identity[F[A]]` が成り立つこと。

1つ目の性質は、関手が射の合成を保存することを意味します。

```scala
import hamcat.Implicits._
import hamcat.util._

// f: isEven
// g: negate
def negate: Boolean => Boolean = b => !b
```

```scala
// fmap(g compose f)
def lifted1 = OptionFunctor.fmap(negate compose isEven)

// fmap(g) compose fmap(f)
def lifted2 = OptionFunctor.fmap(negate) compose OptionFunctor.fmap(isEven)

lifted1 === lifted2
```

例えば、`Option(3)` に対して以下が成り立ちます。

```scala
OptionFunctor.fmap(negate compose isEven)(Option(3)) == (OptionFunctor.fmap(negate) compose OptionFunctor.fmap(isEven))(Option(3))
// res3: Boolean = true
```

<div align="center">

![合成の保存](./images/07_functor_composition.png)

</div>

2つ目の性質は、関手が恒等射を保存することを意味します。

```scala
import hamcat.data.identity
```

```scala
// fmap(identity[A])
def lifted3 = OptionFunctor.fmap(identity[Int])

// identity[F[A]]
def lifted4 = identity[Option[Int]]

lifted3 === lifted4
```

例えば、`Option(3)` に対して以下が成り立ちます。

```scala
OptionFunctor.fmap(identity[Int])(Option(3)) == identity[Option[Int]](Option(3))
// res4: Boolean = true
```

以上の性質は圏の構造を保存する対応を表す性質です。このような2つの性質を**関手性** (functor laws) と呼びます。

### 7.1.3 関手の定義

では、関手の定義を与えましょう。一般に、関手は以下のように定義されます。

---

圏 **C** から圏 **D** への**関手** (functor) `F` とは、以下を満たす対応のことです。

- **C** の射 f: a -> b を D の射 F(f): F(a) -> F(b) すなわち `fmap(f): F[A] => F[B]` に対応させること。
- **C** の射 f, g の合成 `g compose f` について `fmap(g compose f) == fmap(g) compose fmap(f)` が成り立つこと。
- **C** の任意の対象 A の恒等射 `identity[A]` について `fmap(identity[A]) == identity[F[A]]` が成り立つこと。

---

先ほどみたように、2 番目と 3 番目は関手性を表します。

なお、圏 **C** と **D** は同じであってもよく、特に圏 **C** から圏 **C** への関手は**自己関手** (endofunctor) と呼ばれます。Scala 圏における関手は全て、自己関手です。

## 7.2 プログラミングにおける関手

前節では、関手の定義を与えました。本節では、Scala プログラミングにおける関手を考えていきます。

### 7.2.1 Functor 型クラス

関手は Scala において、以下のような型クラス [Functor](https://github.com/taretmch/hamcat/blob/master/src/main/scala/data/Functor.scala) として実装できます。`Functor` 型クラスは、対象関数として型構築子 `F[_]` をもち、射関数として `fmap` メソッドを持ちます。

```scala
// Functor
trait Functor[F[_]] {
  def fmap[A, B](f: A => B): F[A] => F[B]
}
```

`fmap` メソッドは関数を**引き上げる** (lift)、とも言われます。関数 `A => B` は `fmap` によって `F[_]` 上の関数 `F[A] => F[B]` に引き上げられます。

### 7.2.2 Option 関手

`Functor` のインスタンスは、implicit value によって実装することができます。ここでは、`Option` についての `Functor` のインスタンスを定義してみましょう。

Option 関手のインスタンスは、以下のように実装できます。`Functor` に必要なパラメータは型構築子としての `Option` です。また、抽象メソッドである射関数 `fmap` を実装する必要があります。

```scala
/** Option functor */
implicit val OptionFunctor: Functor[Option] = new Functor[Option] {
  def fmap[A, B](f: A => B): Option[A] => Option[B] = _.map(f)
}
```

Option 関手の `fmap` メソッドは `Option#map` メソッドと同じです。実装を見てわかる通り、`fmap` メソッドが関手性を満たすかどうか、つまり圏の構造を維持する対応かどうかは実装によります。定義だけでは `fmap` メソッドが必ず関手性を満たすとは言えませんが、関手性を満たすように `fmap` メソッドを実装しなければいけません。

実際にこのインスタンスを使ってみましょう。本リポジトリでは、型クラスのインスタンスは `hamcat.Implicits` パッケージ内においてあります。コンソールにおいて `hamcat.Implicits._` をインポートすれば、インスタンスが使えるようになります。`fmap` に `Option(3)` と `isEven` (偶数かどうかを判定する関数) を与えると、`Option(3)` の中の値に `isEven` を適用した結果 (すなわち `Some(false)`) が出力されます。

```scala
import hamcat.Implicits._

OptionFunctor.fmap(isEven)(Option(3))
// res5: Option[Boolean] = Some(value = false)
```

なお、毎回 `OptionFunctor.fmap(...)` と書くのは面倒ですし、不便です。この場合、以下のようにシンタックスを定義することによって `Option#fmap` メソッドとして呼び出せるようになります。

```scala
implicit class FunctorOps[F[_], A](v: F[A])(implicit functor: Functor[F]) {
  def fmap[B](f: A => B): F[B] = functor.fmap[A, B](f)(v)
}
```

```scala
Option(3).fmap(isEven)
// res6: Option[Boolean] = Some(value = false)
```

これは Enrich my library パターンと言われるものです。

では、この Option 関手の `fmap` メソッドが関手性を満たすかどうかについて調べてみましょう。

関手性とは、以下が成り立つことでした。

- **C** の射 f, g の合成 `g compose f` について `fmap(g compose f) == fmap(g) compose fmap(f)` が成り立つこと。
- **C** の任意の対象 A の恒等射 `identity[A]` について `fmap(identity[A]) == identity[F[A]]` が成り立つこと。

1つ目について、以下のテストコードで検証します。

```scala
val increment:   Int => Int     = n => n + 1
val isEven:      Int => Boolean = n => n % 2 == 0
def identity[A]: A   => A       = a => a
val none:        Option[Int]    = None

"Option functor" should "射の合成を保存する" in {
  // fmap(g compose f) == fmap(g) compose fmap(f)
  // Case: Some(1)
  assert(
    OptionFunctor.fmap(isEven compose increment)(Option(1))
      ==
    (OptionFunctor.fmap(isEven) compose OptionFunctor.fmap(increment))(Option(1))
  )
  assert(Option(1).fmap(isEven compose increment) == Option(1).fmap(increment).fmap(isEven))

  // Case: None
  assert(
    OptionFunctor.fmap(isEven compose increment)(none)
      ==
    (OptionFunctor.fmap(isEven) compose OptionFunctor.fmap(increment))(none)
  )
  assert(none.fmap(isEven compose increment) == none.fmap(increment).fmap(isEven))
}
```

2つ目については、以下のテストコードで検証します。

```scala
it should "恒等射を恒等射へ写す" in {
  // fmap(identity[A]) == identity[F[A]]
  // Case: Some(1)
  assert(OptionFunctor.fmap(identity[Int])(Option(1)) == identity[Option[Int]](Option(1)))

  // Case: None
  assert(OptionFunctor.fmap(identity[Int])(none) == identity[Option[Int]](none))
}
```

テストを実行してみると、成功しました！ここで実装した `fmap` は関手性を満たしてそうです。

```sh
sbt:hamcat> core/testOnly hamcat.data.FunctorOptionSpec
[info] FunctorOptionSpec:
[info] Option functor
[info] - should 射の合成を保存する
[info] - should 恒等射を恒等射へ写す
[info] Run completed in 452 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 6 s, completed 2020/12/13 10:36:27
```

### 7.2.3 Reader 関手

次の例として、型 `A` を、`A` を返すような任意の関数 `? => A` に変換するような関手を考えます。この関手は Reader 関手と呼ばれます。

Reader 関手で重要なことは、関数も関手であるということです。関数が関手であれば、型 `R` を受け取って `A` を返すような関数 `R => A` があったとき、`A` を `B` に変換する関数 `f: A => B` を与えれば `R` から `B` の関数を取得することができます。

Reader 関手のインスタンスは、以下のように実装できます。対象関数として型構築子 `Function1[R, ?]` を渡し、射関数 `fmap` を実装します。ここで、`Function1` は1変数関数を表す Scala 標準ライブラリの型です。`Function1[R, ?]` は `R => ?` を表します。

```scala
/** Reader functor */
implicit def Function1Functor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
  def fmap[A, B](f: A => B): (R => A) => (R => B) = fa =>
    f compose fa
}
```

(なお、`Function1[R, ?]` という記法は通常だとコンパイルエラーになります。本リポジトリではエラーを回避するため、typelevel 社の [kind-projector](https://github.com/typelevel/kind-projector) というコンパイラプラグインをインストールしています)

`fmap` メソッドは、ただ2つの関数を合成しているだけです。関数 `R => A` があったとき、引数として関数 `A => B` を受け取ると関数 `R => B` が返されます。

これを使うと、例えば以下のようなことができます。

```scala
// 偶数かどうか判定する関数を奇数かどうか判定する関数に変換する
isEven.fmap(negate) (3)
// res7: Boolean = true
```

`compose` が関手の射関数であるのですね。

## 7.3 関手の合成

圏を対象として関手を射とするような圏を考えるとき、射の合成、すなわち関手の合成を定義する必要があります。

Scala 圏における関手は全て自己関手なので、自己関手どうしを合成することができるのかどうかについて考えてみましょう。

例えば、2つの関手 Option と List を合成してみるとどうなるでしょうか。

対象関数は、型 `Int` を Option 関手によって `Option[Int]` に変換し、List 関手によって `List[Option[Int]]` に変換するものとします。

```scala
val intOptionList: List[Option[Int]] = List(Some(1), Some(3), None, Some(4))
// intOptionList: List[Option[Int]] = List(
//   Some(value = 1),
//   Some(value = 3),
//   None,
//   Some(value = 4)
// )
```

次に、射関数は、List 関手の `fmap` メソッドと Option 関手の `fmap` メソッドの合成 `fmapC` と定義します。

```scala
def fmapL[A, B]: (A => B) => List[A] => List[B] = ListFunctor.fmap
def fmapO[A, B]: (A => B) => Option[A] => Option[B] = OptionFunctor.fmap

def fmapC[A, B]: (A => B) => List[Option[A]] => List[Option[B]] = fmapL.compose(fmapO[A, B])
```

この `fmapC` メソッドを用いると、2つの関手によって包まれた型 `Int` 上の関数

```scala
val increment: Int => Int = _ + 1
// increment: Int => Int = <function1>
```

を `List[Option[Int]]` 上の関数として引き上げることができます。

```scala
fmapC(increment)(intOptionList)
// res8: List[Option[Int]] = List(
//   Some(value = 2),
//   Some(value = 4),
//   None,
//   Some(value = 5)
// )
```

これは、`fmap` メソッドを2回呼び出すことに等しいです。

```scala
intOptionList.fmap(_.fmap(increment))
// res9: List[Option[Int]] = List(
//   Some(value = 2),
//   Some(value = 4),
//   None,
//   Some(value = 5)
// )
```

外側の `fmap` は List 関手の射関数で、内側の `fmap` は Option 関手の射関数です。

関手の合成によって定義された射関数 `fmapC` は射の合成を保存しますし、恒等射を保存します。

```scala
// fmap(g compose f) == fmap(g) compose fmap(f)
fmapC(isEven compose increment)(intOptionList) == (fmapC(isEven) compose fmapC(increment))(intOptionList)
// res10: Boolean = true

// fmap(identity[A]) == identity[F[A]]
fmapC(identity[Int])(intOptionList) == identity[List[Option[Int]]](intOptionList)
// res11: Boolean = true
```

したがって、関手の合成もまた、関手であることがわかります。

自己関手の場合のみを取り上げましたが、自己関手でない一般の関手に関してもこれは成り立ちます。興味があれば証明してみてください。

## まとめ

- 関手は、ある圏を、構造を維持しながら別のある圏に変換する対応のこと。
  - 関手の例として、Option、List、Reader などがある。
  - 関手は、対象 A を対象 F[A] に対応させる。
  - 関手は、射 `f: A => B` を射 `fmap(f): F[A] => F[B]` に対応させる。
- 関手が満たす以下の性質のことを、関手性と呼ぶ。
  - 射 f, g の合成 g . f について `fmap(g compose f) == fmap(g) compose fmap(f)` が成り立つこと。
  - 恒等射 `identity[A]` について `fmap(identity[A]) == identity[F[A]]` が成り立つこと。
- Reader 関手は、ある型 `A` に対して、`A` を返す任意の関数 `Function1[?, A]` を対応させる関手である。