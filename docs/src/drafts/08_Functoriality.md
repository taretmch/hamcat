<!-- omit in toc -->
# 目次

- [8. 関手性](#8-関手性)
  - [8.1 双関手](#81-双関手)
    - [8.1.1 直積圏を定義する](#811-直積圏を定義する)
    - [8.1.2 直積圏における射の合成の例](#812-直積圏における射の合成の例)
    - [8.1.3 双関手の一般的な定義](#813-双関手の一般的な定義)
    - [8.1.4 Tuple2 は双関手](#814-tuple2-は双関手)
    - [8.1.5 Either もまた双関手](#815-either-もまた双関手)
  - [8.2 Writer 関手の再登場](#82-writer-関手の再登場)
  - [8.3 Reader 関手](#83-reader-関手)
    - [8.3.1 Function1 は双関手か？](#831-function1-は双関手か)
    - [8.3.2 Function1 に対して first メソッドを定義する](#832-function1-に対して-first-メソッドを定義する)
    - [8.3.3 共変関手と反変関手](#833-共変関手と反変関手)
  - [8.4 Profunctor](#84-profunctor)
  - [8.5 Hom 関手](#85-hom-関手)
- [本章のまとめ](#本章のまとめ)

# 8. 関手性

前章では、圏と圏との間の対応である関手について定義し、いくつかのインスタンスを見ていきました。

本章では関手についてさらに深掘り、今後の議論の際に必要となるいくつかの関手について説明します。

まず、型構築子に2つの型パラメータを持つ関手である双関手について説明します。双関手の一般的な定義は直積圏を用いて与えられるので、直積圏についても説明します。双関手を用れば、積と余積を構成可能であることを示します。

次に、以前見た Writer 圏は、Writer 関手でもあることを説明します。

Writer 関手を見たあとは、Reader 関手について説明します。Reader 関手は前章でも少し見まして、ある型を返すような関数を作る操作が関手であることが言えましたね。ここでは、Reader 関手に与える型パラメータを1つと固定する (返り値の型を指定する) のではなく、2つ指定する (引数と返り値の型を指定する) ことを考え、これと双関手の関係性について見ていきます。実は、2つの型パラメータをとる Reader 関手は、Profunctor と呼ばれる関手の1つであることがわかります。

最後に、Profunctor の具体例であって、圏論において重要な概念である Hom 関手について説明します。

少し難しいかもしれませんが、なるべくコードに落として学んでいければと思います！

## 8.1 双関手

**双関手** (bifunctor) は、型構築子に2つの型パラメータを持つ関手です。

双関手において、対象関数 `F[_, _]` を、2つの型 `A`、`B` を型 `F[A, B]` に対応させるものとして定義します。

また、射関数 `bimap` を、射 `A => C` と `B => D` に対して射 `F[A, B] => F[C, D]` を対応させるものとして定義します。

双関手の型クラスを定義すると、以下のようになります。

```scala
trait Bifunctor[F[_, _]] {

  /** Morphism mappings for Bifunctor */
  def bimap[A, B, C, D](f: A => C)(g: B => D): F[A, B] => F[C, D]

  def first[A, B, C](f: A => C): F[A, B] => F[C, B] = bimap(f)(identity[B])
  def second[A, B, D](g: B => D): F[A, B] => F[A, D] = bimap(identity[A])(g)
}
```

`Bifunctor` 型クラスは、対象関数として型構築子 `F[_, _]` をもち、射関数として `bimap` メソッドをもちます。`first` および `second` は、双関手の型パラメータの1つ目と2つ目の値に対してそれぞれ変換を施すものです。

`bimap` メソッドを定義すれば `first` メソッドと `second` メソッドを実装できますし、`first` メソッドと `second` メソッドを実装すれば `bimap` メソッドを実装することができます。

<div align="center">

![双関手](./images/08_bifunctor.png)

</div>

### 8.1.1 直積圏を定義する

双関手の一般的な定義を与えるために、**直積圏** (product category) という概念を導入します。

2つの圏 `C1` と `C2` に対して、直積圏 `C1 x C2` を考えることができます。

直積圏 `C1 x C2` は、対象を `C1` の対象 `A` と `C2` の対象 `B` のペア `(A, B)` とします。

そして、射を `C1` の射 `f: A -> C` と `C2` の射 `g: B -> D` のペア `(f, g)` とします。本リポジトリでは、関数のペアをラッパークラスとして実装してみました：

```scala
case class ProductFunction[A, B, C, D](run: (A => C, B => D))
```

どちらも、`C1` と `C2` の対象と射のペアをとっているだけですね。

射の合成についてもペアをとるだけです。`C1` における射の合成 `h compose f` と `C2` における射の合成 `k compose g` に対して、`(h compose f, k compose g)` は直積圏 `C1 x C2` における射の合成になります：

```scala
/** Composition of morphism in product category */
def andThen[E, H](v: ProductFunction[C, D, E, H]): ProductFunction[A, B, E, H] =
  run match {
    case (f, g) => v.run match {
      case (h, k) => ProductFunction((f andThen h, g andThen k))
    }
  }

/** Composition of morphism in product category */
def compose[E, H](v: ProductFunction[E, H, A, B]): ProductFunction[E, H, C, D] =
  run match {
    case (f, g) => v.run match {
      case (h, k) => ProductFunction((f compose h, g compose k))
    }
  }
```

上記のように射の合成 `andThen` メソッドと `compose` メソッドを定義すると、Scala の直積圏における2つの射 `(A => C, B => D)` と `(C => E, D => H)` を合成して `(A => E, B => H)` を構成できます。

恒等射も同様に、`C1` の恒等射 `identityC1` と `C2` の恒等射 `identityC2` に対して `(identityC1, identityC2)` が直積圏の恒等射になります。

```scala
/** Identity morphism */
def productIdentity[A, B]: ProductFunction[A, B, A, B] =
  ProductFunction(identity[A], identity[B])
```

### 8.1.2 直積圏における射の合成の例

`ProductFunction` クラスは、Scala 圏と Scala 圏の直積圏の実装です。

対象は、2つの Scala 圏の対象、すなわち型 `A` と型 `B` のタプルです。ここでは `A` を `Int` とし、`B` を `Long` としています。

```scala mdoc
/** Object declaration */
val obj = (3, 4L)
```

直積圏における射 `func1` と `func2` は、Scala 圏の2つの射、すなわち関数 `A => C` 関数 `B => D` のタプルです。`func1` は第1引数として `Int` 型のインクリメント関数 `increment` を持ち、第2引数として `Long` 型の数を2倍する関数 `doubleL` を持ちます。`func2` は第1引数として `Int` 型の数が偶数かどうか判定する関数 `isEven` を持ち、第2引数として `Long` 型の数が奇数かどうか判定する関数 `isOddL` を持ちます。


```scala mdoc
def increment: Int => Int = _ + 1
def doubleL: Long => Long = _ * 2
def isEven: Int => Boolean = _ % 2 == 0
def isOddL: Long => Boolean = _ % 2 == 1
```

```scala mdoc
import hamcat.arrow.ProductFunction

/** Morphism declaration */
val func1 = ProductFunction(increment, doubleL)
val func2 = ProductFunction(isEven, isOddL)
```

それぞれの関数の定義は以下のようになっています。

`ProductFunction` クラスには関数適用のために `apply` メソッドをはやしているので、以下のように関数適用の結果を出力できます。

```scala mdoc
/** Apply morphism to object */
val func1Apply: (Int, Long)        = func1(obj)
val func2Apply: (Boolean, Boolean) = func2(obj)
```

この直積圏における射の合成は、先ほど定義した `andThen` メソッドおよび `compose` メソッドを使って構築できます。この合成関数は、第1引数として `Int` 型の数が奇数かどうか判定する（インクリメントして偶数かどうか判定するので）関数を持ち、第2引数として常に `false` を返す（数を2倍したあと奇数かどうかを判定するので）関数を持ちます。

```scala mdoc
/** Compose morphism */
def func2ComposeFunc1 = func2 compose func1
def func1AndThenFunc2 = func1 andThen func2
```

これらの関数に `(3, 4L)` を適用すると以下の結果が返ります。

```scala mdoc
/** Apply composition of morphism */
val result1 = func2ComposeFunc1(obj)
val result2 = func1AndThenFunc2(obj)
```

### 8.1.3 双関手の一般的な定義

さて、2つの圏の対象と射、射の合成をそれぞれペアにすることによって、直積圏を定義しました。双関手の話に戻りましょう。

一般的に、双関手は以下のように定義されます。

---

**双関手** (bifunctor) とは、2つの圏 `C` と `D` の直積圏 `C x D` から圏 `E` への関手のことです。

---

Scala 圏において関手は自己関手となるので、Scala 圏における双関手 (すなわち Bifunctor) は Scala 圏と Scala 圏の直積から Scala 圏への関手になります。

すなわち、`Bifunctor` は、対象関数 `F[_, _]` として Scala の直積圏の対象 `A` と `B` を `F[A, B]` に対応させ、射関数 `bimap` として Scala の直積圏の射 `A => C` と `B => D` を `F` に関する射 `F[A, B] => F[C, D]` に対応させます。

なお、`bimap` の引数が `(A => C, B => D)` ではなく `A => C` と `B => D` であるのは、使いやすさの観点からです。これらは、互いに同型であるので、どちらの形でも問題はありません。

```scala mdoc
def isomorpTupleToFunc1[A, B, C, D]: ((A => C, B => D)) => (A => C) => (B => D) = {
  case (f, g) => f => g
}

def isomorpFuncToTuple[A, B, C, D]: (A => C) => (B => D) => ((A => C, B => D)) =
  f => g => (f, g) 
```

では、双関手のいくつかの例をみていきましょう。

### 8.1.4 Tuple2 は双関手

積は、2つの型パラメータから構築されます。

```scala mdoc
val tuple: Tuple2[Int, String] = (33, "thirty three")
```

`Tuple2` は2つの型パラメータを持つため、双関手の候補になります。

実際、積を構築する**積関手** `Tuple2` は、双関手の例です。

Scala の直積圏から型を1つずつとってきて、型構築子 `Tuple2[_, _]` によって Scala 圏の型 `Tuple2` を構成します。

`Tuple2` の射関数 `bimap` は以下のように定義されます。すなわち、直積圏の射 `f: A => C`, `g: B => D` が与えられると、それらを `Tuple2` に関する射 `Tuple2[A, B] => Tuple2[C, D]` に引き上げます。

```scala
/** Product bifunctor */
implicit val Tuple2Bifunctor = new Bifunctor[Tuple2] {
  def bimap[A, B, C, D](f: A => C)(g: B => D): ((A, B)) => ((C, D)) = {
    case (a, b) => (f(a), g(b))
  }
}
```

### 8.1.5 Either もまた双関手

余積も積と同様、2つの型パラメータから構築されます。

```scala mdoc
val right: Either[Int, String] = Right("thirty three")
```

余積を構築する**余積関手** `Either` は、双関手の例です。

積と同様に、Scala の直積圏から型を1つずつとってきて、型構築子 `Either[_, _]` によって Scala 圏の対象 `Either` 型に対応させます。

`Either` の射関数 `bimap` は以下のように定義されます。すなわち、直積圏の射 `f: A => C`, `g: B => D` が与えられると、それらを `Either` に関する射 `Either[A, B] => Either[C, D]` に引き上げます。

```scala
/** Coproduct bifunctor */
implicit val EitherBifunctor = new Bifunctor[Either] {
  def bimap[A, B, C, D](f: A => C)(g: B => D): Either[A, B] => Either[C, D] = {
    case Left(a)  => Left(f(a))
    case Right(b) => Right(g(b))
  }
}
```

## 8.2 Writer 関手の再登場

4章で、Kleisli 圏の例として Writer 圏を見ました。Writer 圏において、以下のような型 `Writer` を導入しました。

```scala
// L: Monoid
type Writer[L, A] = (L, A)
```

Writer 圏における対象は任意の型 `A` で、`A` から `A` への射は `A => Writer[L, A]` だと定義しました。

実は、Writer 圏における射の合成をうまく活用することによって、`Writer` 型についての `fmap` メソッドを実装することができます。そのため、`Writer` 型は関手であって、Writer 関手と呼ばれます。

```scala
/** Writer functor */
implicit def WriterFunctor[L](implicit mn: Monoid[L]): Functor[Writer[L, ?]] = new Functor[Writer[L, ?]] {
  def fmap[A, B](f: A => B)(fa: Writer[L, A]): Writer[L, B] =
    (identity[Writer[L, A]] >=> (a => Writer.pure[L, B](f(a))))(fa)
}
```

`identity[Writer[L, A]] >=> ` が使えることに驚くかもしれませんが、うまくいきます。`>=>` は `A => Writer[L, B]` の形をした関数が持つメソッドです。`identity[Writer[L, A]]: Writer[L, A] => Writer[L, A]` はこの形をしているので、`>=>` を呼び出すことができます。

## 8.3 Reader 関手

前章では型 `A` を `R => A` に対応させ、関数 `A => B` を `R => B` に引き上げる Reader 関手を考えました。

```scala
/** Reader functor */
implicit def Function1Functor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
  def fmap[A, B](f: A => B)(fa: R => A): (R => B) =
    f compose fa
}
```

この型構築子 `Function1[_, _]` は2つの型パラメータを持つので、双関手の候補であると考えられます。

ここでは、`Function1` を双関手のインスタンスとして実装できるかどうかについて考えていきましょう。

### 8.3.1 Function1 は双関手か？

`Bifunctor` のインスタンスでは、`bimap` を実装する必要があります。ただ、`bimap` は `first` および `second` を実装することによって、これらを組み合わせて実装することができます。

```scala
trait Bifunctor[F[_, _]] {

  /** Morphism mappings for Bifunctor */
  def bimap[A, B, C, D](f: A => C)(g: B => D): F[A, B] => F[C, D] =
    second(g) compose first(f)

  def first[A, B, C](f: A => C): F[A, B] => F[C, B]
  def second[A, B, D](g: B => D): F[A, B] => F[A, D]
}
```

まず、`Bifunctor` の `second` メソッドは、前章でみた Reader 関手の射関数です。

```scala
/** Reader bifunctor ? */
implicit val Function1MaybeBifunctor = new Bifunctor[Function1] {
  override def second[A, B, C, D](g: B => D): (A => B) => (A => D) =
    fab => g compose fab
}
```

では、`first` メソッドはどうでしょうか？

```scala
def first[A, B, C](f: A => C)(fa: A => B) => (C => B) = ???
```

シグネチャを見ると分かる通り、関数 `A => C` と `A => B` の組み合わせでは `C => B` を構成することができません。

`first` メソッドを定義できない、すなわち射関数 `bimap` を定義できないことから、`Function1` は双関手でないと言えます。

### 8.3.2 Function1 に対して first メソッドを定義する

前項で見た通り、関数 `A => C` と `A => B` からは `C => B` を構成することはできませんでした。

```scala
override def first[A, B, C](f: A => C): (A => B) => (C => B) = ???
```

しかしながら、関数 `f: A => C` の矢印を反転させて `oppF: C => A` を受け取れば、`first` メソッドを定義できます。

```scala
override def first[A, B, C](oppF: C => A): (A => B) => (C => B) = fa => fa compose oppF
```

少し言い換えると、直積圏における第1要素の射の矢印を入れ替えれば、第1要素の射を `Function1` に関する射に引き上げることができました。

さて、ある圏において、対象はそのままで射の矢印を入れ替えたものを、その圏の双対圏と呼ぶことができましたね。これはすなわち、**Function1 が Scala の双対圏と Scala 圏の直積圏から Scala 圏への関手となっている**と言うことができます。`first` メソッドは、Scala の双対圏からの射関手であると言えます。

### 8.3.3 共変関手と反変関手

一般に、ある圏 `C` の双対圏 `oppC` からある圏 `D` への関手のことを**共変関手** (contravariant functor) と呼びます。

一方で、これまで話してきた標準の関手は**反変関手** (covariant functor) と呼ばれます。

反変関手と同様に、共変関手の型クラス `Contravariant` は以下のように定義されます。対象関数 `F[_]` は反変関手と同じです。射関数 `contramap` は、射 `B => A` を `F` に関する射 `F[A] => F[B]` に引き上げます。

```scala
// Contravariant functor
trait Contravariant[F[_]] {
  def contramap[A, B](f: B => A)(fa: F[A]): F[B]
}
```

`Function1` の第1引数に対して `Contravariant` のインスタンスを以下のように実装できます。型の変数は違うものの、先ほどの見た `first` メソッドと同じ実装になっているはずです。

```scala
/** Reader contravariant functor */
implicit def Function1Contravariant[R]: Contravariant[Function1[?, R]] = new Contravariant[Function1[?, R]] {
  def contramap[A, B](f: B => A)(fa: A => R): (B => R) =
    fa compose f
}
```

```scala
override def first[A, B, C](oppF: C => A): (A => B) => (C => B) = fa => fa compose oppF
```

## 8.4 Profunctor

共変関手と反変関手の概念を用いると、型構築子 `Function1[_, _]` は第1引数に関して共変であり、第2引数に関して反変であると言われます。

このように、1つ目の型パラメータが共変で2つ目の型パラメータが反変であるような型構築子は、**Profunctor** と呼ばれます。Profunctor も関手であって、対象関数は `F[_, _]` です。射関数 `bimap` は、第1引数 `C => A` を共変関手のように引き上げ、第2引数 `B => D` を反変関手のように引き上げることによって `F[A, B] => F[C, D]` に引き上げます。

```scala
// Profunctor
trait Profunctor[F[_, _]] {

  def bimap[A, B, C, D](f: C => A)(g: B => D)(fab: F[A, B]): F[C, D]
}
```

先ほど見たように、`Function1` は `Profunctor` です。`Function1` の `Profunctor` のインスタンスは、以下のように定義されます。

```scala
/** Reader profunctor */
implicit val Function1Profunctor = new Profunctor[Function1] {
  def bimap[A, B, C, D](f: C => A)(g: B => D)(fab: A => B): (C => D) =
    g compose fab compose f
}
```

なお、Profunctor の一般的な定義は双対圏を用いて与えられます：

---

圏 `C` から `D` への Profunctor とは、`D` の双対圏 `oppD` と `C` の直積圏 `oppD x C` から集合圏 `Set` への関手です。

---

Scala 圏は集合圏の拡張であるため、Scala 圏の双対圏と Scala 圏の直積から Scala 圏への関手を Profunctor と定義することができます。

## 8.5 Hom 関手

本章の最後に、圏論における重要な概念である **Hom 集合** (hom set) について紹介します。一般に、ある圏 `C` の Hom 集合は、`C` の全ての射の集まり `C(A, B)` （`A` と `B` は `C` における任意の対象）として定義されます。

Scala 圏における Hom 集合は、射 `A => B` の集まり、すなわち `Function1[A, B]` です。`Function1[A, B]` を構成する操作は Profunctor で、Scala の双対圏と Scala 圏の直積圏から Scala 圏への関手のことでした。

これと同様に、Hom 集合を構成する操作も Profunctor であって、**Hom 関手** と呼ばれます。Profunctor として捉えると、ある圏 `C` の Hom 関手とは、直積圏 `oppC x C` から集合圏 `Set` への関手であると考えられます。

`oppC x C` の射 `(oppF: E => A, g: B => F)` を Hom 関手によって引き上げると、Hom 集合 `C(A, B)` から Hom 集合 `C(E, F)` の集合に変換されます。ここで、`C(A, B)` のなんらかの要素 `h` （これは射 `A => B` の一つ）をとってきて

```
g compose h compose oppF
```

とすると、これは射 `E => F` となり、`C(E, F)` の要素が得られます。

# 本章のまとめ

- 2つの圏 `C` と `D` の直積圏とは、対象・射・射の合成それぞれを `C` と `D` のペアとして定義した圏のことである。
  - 対象：`(A, B)`
  - 射：`(A => C, B => D)`
  - 射の合成：`((C => E) compose (A => C), (D => H) compose (B => D))`
- 双関手は型構築子に2つの型パラメータを持った関手である。
  - 直積圏からの関手と定義される：`C x D -> E`
  - 積関手である Tuple2 は、双関手である。
  - 余積関手である Either は、双関手である。
- Writer も関手である。
- Function1 は型構築子に2つの型パラメータを持つが、双関手ではない。
- 双対圏からの関手を共変関手という。
- 標準の圏からの関手を反変関手という。
- 第1引数が共変で、第2引数が反変な関手を Profunctor という。
  - Function1 は Profunctor である。
  - Profunctor は、双対圏との直積圏から集合圏への関手として定義される：`oppC x D -> Set`
- ある圏 `C` の Hom 集合とは、`C` の全ての射の集まりのことである。
  - Scala 圏の Hom 集合は `Function1[A, B]` である。
  - 2つの対象 `A` と `B` から Hom 集合 `C(A, B)` を作る操作は関手であり、Hom 関手という。
  - Hom 関手は、Profunctor である：`oppC x C -> Set`
