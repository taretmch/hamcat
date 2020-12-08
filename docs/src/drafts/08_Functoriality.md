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

```scala
trait Bifunctor[F[_, _]] {

  /** Morphism mappings for Bifunctor */
  def bimap[A, B, C, D](f: A => C)(g: B => D): F[A, B] => F[C, D]

  def first[A, B, C](f: A => C): F[A, B] => F[C, B] = bimap(f)(identity[B])
  def second[A, B, D](g: B => D): F[A, B] => F[A, D] = bimap(identity[A])(g)
}
```

双関手は数学的に、以下のように定義されます。

---

2つの圏 `C` と `D` の直積圏 `C x D` から圏 `E` への関手を双関手と呼びます。

<!-- TODO: 直積圏の定義 -->

---

### 8.1.1 積関手

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

### 8.1.2 余積関手

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

