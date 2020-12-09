# 8. 関手性

前章では、圏と圏との間の対応である関手について定義し、いくつかのインスタンスを見ていきました。

本章では関手についてさらに深掘り、今後の議論の際に必要となるいくつかの関手について説明します。

まず、型構築子に2つの型パラメータを持つ関手である双関手について説明し、これによって積と余積を構成可能であることを示します。

次に、以前見た Writer 圏は、Writer 関手でもあることを説明します。

Writer 関手を見たあとは、Reader 関手について説明します。Reader 関手は前章でも少し見まして、ある型を返すような関数を作る操作が関手であることが言えましたね。ここでは、Reader 関手に与える型パラメータを1つと固定する (返り値の型を指定する) のではなく、2つ指定する (引数と返り値の型を指定する) ことを考え、これと双関手の関係性について見ていきます。実は、2つの型パラメータをとる Reader 関手は、Profunctor と呼ばれる関手の1つであることがわかります。

最後に、Profunctor の具体例であって、圏論において重要な概念である Hom 関手について説明します。

少し難しいかもしれませんが、なるべくコードに噛み砕いて学んでいければと思います！

## 8.1 双関手

**双関手** (bifunctor) は、型構築子に2つの型パラメータを持つ関手です。

双関手において、対象関数を、2つの型 `A`、`B` を型 `F[A, B]` に対応させるものとして定義します。

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

### 8.1.1 直積圏

双関手の厳密な定義を与えるために、直積圏 (product category) という概念を導入します。

2つの圏 `C` と `D` に対して、直積圏 `C x D` というものを考えることができます。

直積圏 `C x D` は、対象を `C` の対象 `c` と `D` の対象 `d` のペア `(c, d)` とします。

```scala mdoc
/** Object type of product category */
type BiObj[A, B] = (A, B)
```

そして、射を `C` の射 `f: c1 -> c2` と `D` の射 `g: d1 -> d2` のペア `(f, g)` とします。

```scala mdoc
/** Morphism in product category */
def biMorp[A, B, C, D](f: A => C)(g: B => D): BiObj[A, B] => BiObj[C, D] = {
  case (a, b) => (f(a), g(b))
}

```

どちらも、`C` と `D` の対象と射のペアをとっているだけですね。

射の合成についてもペアをとるだけです。`C` における射の合成 `f' compose f` と `D` における射の合成 `g' compose g` に対して、`(f' compose f, g' compose g)` は直積圏 `C x D` における射の合成になります。

射の合成 `andThen` メソッドを以下のように定義すると、2つの射 `BiObj[A, B] => BiObj[C, D]` と `BiObj[C, D] => BiObj[E, H]` を合成して `BiObj[A, B] => BiObj[E, H]` を構成できます。

```scala mdoc
/** Composition of morphism */
implicit class Ops[A, B, C, D](lhs: BiObj[A, B] => BiObj[C, D]) {
  def andThen[E, H](rhs: BiObj[C, D] => BiObj[E, H]): BiObj[A, B] => BiObj[E, H] = {
    case (a, b) => rhs(lhs((a, b)))
  }
}
```

恒等射も同様に、`C` の恒等射 `identityC` と `D` の恒等射 `identityD` に対して `(identityC, identityD)` が直積圏の恒等射になります。

```scala mdoc
/** Identity morphism */
def pure[A, B](obj: BiObj[A, B]): BiObj[A, B] = biMorp(identity[A])(identity[B])(obj)
```

Scala 圏と Writer 圏の直積圏 `Scala x Writer` を例に考えてみましょう。

対象 `biObj` は、Scala 圏の対象、すなわち型 `A` と Writer 圏の対象、すなわち型 `B` のタプルです。

```scala mdoc
import category.universal.ProductCategory._

/** Object declaration */
val biObj: BiObj[Int, Long] = (2, 4L)
```

射 `biMorp1` は、Scala 圏の射、すなわち関数 `A => C` と Writer 圏の射、すなわち関数 `B => Writer[L, D]` のタプルです。

```scala mdoc
import category.universal.ProductCategory
import category.data.Writer
import category.Implicits._

def increment: Int => Int = _ + 1
def doubleL: Long => Writer[String, Long] = longNum => Writer("double ", longNum * 2)

/** Morphism declaration */
val biMorp1: BiObj[Int, Long] => BiObj[Int, Writer[String, Long]] = ProductCategory.biMorp(increment)(doubleL)
```

この射の合成 `biComp` は、先ほど定義した `andThen` メソッドを使って構築できそうですが、Writer 圏の射は `andThen` では合成できません。Writer 圏の合成を行うために、`BiObj[A, B] => BiObj[C, Writer[L, D]]` 型に `>=>` メソッドを生やすと以下のように `biComp` を構成できます。

```scala mdoc
def isEven: Int => Boolean = _ % 2 == 0
def isEvenL: Long => Writer[String, Boolean] = longNum => Writer("isEven ", longNum % 2 == 0)

val biMorp2: BiObj[Int, Long] => BiObj[Boolean, Writer[String, Boolean]] = ProductCategory.biMorp(isEven)(isEvenL)
```

```scala
/** Compose morphism */
val biComp: BiObj[Int, Long] => BiObj[Boolean, Writer[String, Boolean]] = biMorp1 >=> biMorp2

/** Apply composition of morphism */
val biCompApply: BiObj[Boolean, Writer[String, Boolean]] = biComp(biObj)
// biCompApply: (Boolean, category.data.Writer[String,Boolean]]) = (false, Writer((double isEven ,true)))
```

### 8.1.2 双関手の定義

双関手は、以下のように定義されます。

---

**双関手** (bifunctor) とは、2つの圏 `C` と `D` の直積圏 `C x D` から圏 `E` への関手のことです。

---

Scala 圏において関手は自己関手となるので、Scala 圏における双関手 (すなわち Bifunctor) は Scala 圏と Scala 圏の直積から Scala 圏への関手になります。

### 8.1.3 積関手

積は、2つの型パラメータから構築されます。

```scala mdoc
val tuple: Tuple2[Int, String] = (33, "thirty three")
```

積を構築する関手 `Tuple2` は、双関手の例です。

```scala
/** Product bifunctor */
implicit val Tuple2Bifunctor = new Bifunctor[Tuple2] {
  def bimap[A, B, C, D](f: A => C)(g: B => D): ((A, B)) => ((C, D)) = {
    case (a, b) => (f(a), g(b))
  }
}
```

### 8.1.4 余積関手

余積も積と同様、2つの型パラメータから構築されます。

```scala mdoc
val right: Either[Int, String] = Right("thirty three")
```

余積を構築する関手 `Either` は、双関手の例です。

```scala
/** Coproduct bifunctor */
implicit val EitherBifunctor = new Bifunctor[Either] {
  def bimap[A, B, C, D](f: A => C)(g: B => D): Either[A, B] => Either[C, D] = {
    case Left(a)  => Left(f(a))
    case Right(b) => Right(g(b))
  }
}
```

## 8.2 Writer 関手

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
    fa.flatMap((Writer.pure[L, B] _) compose f)
}
```

## 8.3 Reader 関手

前章では、

```scala
/** Reader functor */
implicit def Function1Functor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
  def fmap[A, B](f: A => B)(fa: R => A): (R => B) =
    f compose fa
}
```

### 8.3.1 反変関手

### 8.3.2 共変関手

## 8.4 Profunctor

## 8.5 Hom 関手

