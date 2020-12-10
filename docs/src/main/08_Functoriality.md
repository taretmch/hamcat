# 8. 関手性

前章では、圏と圏との間の対応である関手について定義し、いくつかのインスタンスを見ていきました。

本章では関手についてさらに深掘り、今後の議論の際に必要となるいくつかの関手について説明します。

まず、型構築子に2つの型パラメータを持つ関手である双関手について説明します。双関手の一般的な定義は直積圏を用いて与えられるので、直積圏についても説明します。双関手を用れば、積と余積を構成可能であることを示します。

次に、以前見た Writer 圏は、Writer 関手でもあることを説明します。

Writer 関手を見たあとは、Reader 関手について説明します。Reader 関手は前章でも少し見まして、ある型を返すような関数を作る操作が関手であることが言えましたね。ここでは、Reader 関手に与える型パラメータを1つと固定する (返り値の型を指定する) のではなく、2つ指定する (引数と返り値の型を指定する) ことを考え、これと双関手の関係性について見ていきます。実は、2つの型パラメータをとる Reader 関手は、Profunctor と呼ばれる関手の1つであることがわかります。

最後に、Profunctor の具体例であって、圏論において重要な概念である Hom 関手について説明します。

少し難しいかもしれませんが、なるべくコードに噛み砕いて学んでいければと思います！

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

### 8.1.1 直積圏を定義する

双関手の一般的な定義を与えるために、**直積圏** (product category) という概念を導入します。

2つの圏 `C` と `D` に対して、直積圏 `C x D` を考えることができます。

直積圏 `C x D` は、対象を `C` の対象 `c` と `D` の対象 `d` のペア `(c, d)` とします。

```scala
/** Object type of product category */
type BiObj[A, B] = (A, B)
```

そして、射を `C` の射 `f: c1 -> c2` と `D` の射 `g: d1 -> d2` のペア `(f, g)` とします。

```scala
/** Morphism in product category */
def biMorp[A, B, C, D](f: A => C)(g: B => D): (A => C, B => D) = (f, g)
```

どちらも、`C` と `D` の対象と射のペアをとっているだけですね。

射の合成についてもペアをとるだけです。`C` における射の合成 `f' compose f` と `D` における射の合成 `g' compose g` に対して、`(f' compose f, g' compose g)` は直積圏 `C x D` における射の合成になります。

射の合成 `andThen` メソッドを以下のように定義すると、Scala の直積圏における2つの射 `(A => C, B => D)` と `(C => E, D => H)` を合成して `(A => E, B => H)` を構成できます。なお、2つの関数のタプルを適用するために、`apply` メソッドも定義しておきます。

```scala
/** Enrich bimorp */
implicit class BiMorpOps[A, B, C, D](lhs: (A => C, B => D)) {
  /** Composition of morphism in product category */
  def andThen[E, H](rhs: (C => E, D => H)): (A => E, B => H) =
    lhs match {
      case (f, g) => rhs match {
        case (h, k) => (h compose f, k compose g)
      }
    }

  /** Composition of morphism in product category */
  def compose[E, H](rhs: (E => A, H => B)): (E => C, H => D) =
    lhs match {
      case (f, g) => rhs match {
        case (h, k) => (f compose h, g compose k)
      }
    }

  /** Apply method for bimorp */
  def apply(obj: BiObj[A, B]): BiObj[C, D] = lhs match {
    case (f, g) => obj match {
      case (a, b) => (f(a), g(b))
    }
  }
}
```

恒等射も同様に、`C` の恒等射 `identityC` と `D` の恒等射 `identityD` に対して `(identityC, identityD)` が直積圏の恒等射になります。

```scala
import category.Implicits._

/** Identity morphism */
def biIdentity[A, B](obj: BiObj[A, B]): BiObj[A, B] =
  biMorp(identity[A])(identity[B])(obj)
```

### 8.1.2 直積圏における射の合成の例

Scala における圏は Scala 圏ですので、Scala 圏と Scala 圏の直積圏を例に考えてみましょう。

対象 `biObj` は、2つの Scala 圏の対象、すなわち型 `A` と型 `B` のタプルです。ここでは `A` を `Int` とし、`B` を `Long` としています。

```scala
import category.universal.ProductCategory._

/** Object declaration */
val biObj: BiObj[Int, Long] = (3, 4L)
// biObj: (Int, Long) = (3, 4L)
```

直積圏における射 `biMorp1` と `biMorp2` は、Scala 圏の2つの射、すなわち関数 `A => C` 関数 `B => D` のタプルです。`biMorp1` は第1引数として `Int` 型のインクリメント関数を持ち、第2引数として `Long` 型の数を2倍する関数を持ちます。`biMorp2` は第1引数として `Int` 型の数が偶数かどうか判定する関数を持ち、第2引数として `Long` 型の数が偶数かどうか判定する関数を持ちます。

```scala
/** Morphism declaration */
val biMorp1 = biMorp(increment)(doubleL)
// biMorp1: (Int => Int, Long => Long) = (<function1>, <function1>)
val biMorp2 = biMorp(isEven)(isEvenL)
// biMorp2: (Int => Boolean, Long => Boolean) = (<function1>, <function1>)
```

それぞれの関数の定義は以下のようになっています。

```scala
def increment: Int => Int = _ + 1
def doubleL: Long => Long = _ * 2
def isEven: Int => Boolean = _ % 2 == 0
def isEvenL: Long => Boolean = _ % 2 == 1
```

この直積圏における射の合成 `biComp` は、先ほど定義した `andThen` メソッドを使って構築できます。この関数 `biComp` は、第1引数として `Int` 型の数が奇数かどうか判定する（インクリメントして偶数かどうか判定するので）関数を持ち、第2引数として常に `true` を返す（数を2倍したあと偶数かどうかを判定するので）関数を持ちます。

```scala
/** Compose morphism */
val biComp: (Int => Boolean, Long => Boolean) = biMorp1 andThen biMorp2
// biComp: (Int => Boolean, Long => Boolean) = (
//   scala.Function1$$Lambda$6839/701532719@879df5b,
//   scala.Function1$$Lambda$6839/701532719@44ee014
// )
```

この関数に `(3, 4L)` を適用すると以下の結果が返ります。

```scala
val result: (Boolean, Boolean) = biComp(biObj)
// result: (Boolean, Boolean) = (true, false)
```

### 8.1.3 双関手の一般的な定義

さて、2つの圏の対象と射、射の合成をそれぞれペアにすることによって、直積圏を定義しました。双関手の話に戻りましょう。

一般的に、双関手は以下のように定義されます。

---

**双関手** (bifunctor) とは、2つの圏 `C` と `D` の直積圏 `C x D` から圏 `E` への関手のことです。

---

Scala 圏において関手は自己関手となるので、Scala 圏における双関手 (すなわち Bifunctor) は Scala 圏と Scala 圏の直積から Scala 圏への関手になります。

すなわち、`Bifunctor` は、対象関数 `F[_, _]` として Scala の直積圏の対象 `A` と `B` を `F[A, B]` に対応させ、射関数 `bimap` として Scala の直積圏の射 `A => C` と `B => D` をに関する射 `F[A, B] => F[C, D]` に対応させます。

では、双関手のいくつかの例をみていきましょう。

### 8.1.4 Tuple2 は双関手

積は、2つの型パラメータから構築されます。

```scala
val tuple: Tuple2[Int, String] = (33, "thirty three")
// tuple: (Int, String) = (33, "thirty three")
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

```scala
val right: Either[Int, String] = Right("thirty three")
// right: Either[Int, String] = Right(value = "thirty three")
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

## 8.3 Writer 関手を見たあとは Reader 関手を考える

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

まず、`Bifunctor` の `second` メソッドは、前章でみた Reader 関手の射関数です。

```scala
/** Reader bifunctor ? */
implicit val Function1MaybeBifunctor = new Bifunctor[Function1] {
  def second[A, B, C, D](g: B => D)(fab: A => B): (A => D) =
    g compose fab
}
```

では、`first` メソッドはどうでしょうか？

```scala
def first[A, B, C](f: A => C)(fa: A => B) => (C => B) = ???
```

シグネチャを見ると分かる通り、関数 `A => B` と `A => C` の組み合わせでは `C => B` を構成することができません。

`first` メソッドを定義できない、すなわち射関数 `bimap` を定義できないことから、`Function1` は双関手でないと言えます。

### 8.3.2 Function1 に対して first メソッドを定義する

前項で見た通り、関数 `A => B` と `A => C` からは `C => B` を構成することはできませんでした。

```scala
def first[A, B, C](f: A => C)(fa: A => B) => (C => B) = ???
```

しかしながら、関数 `f: A => C` の矢印を反転させて `oppositeF: C => A` を受け取れば、`first` メソッドを定義できます。

```scala
def first[A, B, C](f: C => A)(fa: A => B) => (C => B) = fa compose f
```

少し言い換えると、直積圏における第1要素の射の矢印を入れ替えれば、第1要素の射を `Function1` に関する射に引き上げることができました。

さて、ある圏において、対象はそのままで射の矢印を入れ替えたものを、その圏の双対圏と呼ぶことができましたね。これはすなわち、`Function1` が Scala の双対圏と Scala 圏の直積圏から Scala 圏への関手となっていると言うことができます。`first` メソッドは、Scala の双対圏からの射関手であると言えます。

### 8.3.3 共変関手と反変関手

一般に、ある圏 `C` の双対圏 `oppC` からある圏 `D` への関手のことを**共変関手** (contravariant functor) と言います。

一方で、これまで話してきた標準の関手は**反変関手** (covariant functor) と呼ばれます。

反変関手と同様に、共変関手の型クラス `Contravariant` は以下のように定義されます。対象関数は反変関手と同じです。射関数 `contramap` は、射 `B => A` を `F` に関する射 `F[A] => F[B]` に引き上げます。

```scala
// Contravariant functor
trait Contravariant[F[_]] {
  def contramap[A, B](f: B => A)(fa: F[A]): F[B]
}
```

`Function1` の第1引数に対して `Contravariant` のインスタンスを以下のように実装できます。先ほどの見た `first` メソッドと同じ実装になっているはずです。

```scala
/** Reader contravariant functor */
implicit def Function1Contravariant[R]: Contravariant[Function1[?, R]] = new Contravariant[Function1[?, R]] {
  def contramap[A, B](f: B => A)(fa: A => R): (B => R) =
    fa compose f
}
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
