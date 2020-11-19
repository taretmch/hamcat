# 7. 関手

圏は対象の集まりと射の集まりからなりますが、これまでに何度か「対象を圏として圏を構成できるのではないか？」と思った人もいるのではないでしょうか。

その疑問に対する答えは「できる」です。今回定義する関手を使えば、圏の構造（つまり、圏の図式の形）を維持したまま別の圏に変換できるようになります。

関手は、非常に単純ですが強力な概念です。本章では、関手とは何かについて定義し、プログラミングにおける関手の例を示します。

## 7.1 関手とは

関手 (functor) は、ある圏を別のある圏への対応のことです。

関手の例としては Option 関手、List 関手、Writer 関手などがあります。

Option 関手は、型 `A` の値を `Option` で包んで型 `Option[A]` に変換します。また、型 `A` から型 `B` への関数を `Option[A]` から `Option[B]` への関数に変換します。

同様に、List 関手は型 `A` を `List[A]` に変換し、関数 `A => B` を `List[A] => List[B]` に変換します。Writer 関手も型 `A` を `Writer[A]` に変換し、関数 `A => B` を `Writer[A] => Writer[B]` に変換します。

圏は対象と射から構成されるので、圏を変換するには対象と射それぞれに関する対応を定義する必要があります。

<!--- 関手の集合における例が欲しい。あと有向グラフとか。 -->

### 7.1.1 対象関数

関手において、ある圏の対象を別のある圏の対象に変換するような対応を対象関数といいます。一般に、圏 C から D への関手 F は、圏 C の対象 A を D の対象 F A に対応させます。

Option 関手の例で言うと、Option 関手はScala 圏から Scala 圏への関手で、対象 `A` を対象 `Option[A]` に対応させています。

```scala
def objOptFunc[A]: A =>  Option[A] = Option(_)

objOptFunc(3)
// res0: Option[Int] = Some(value = 3)
objOptFunc("Hoge")
// res1: Option[String] = Some(value = "Hoge")
```

### 7.1.2 射関数

関手において、ある圏の射を別のある圏の射に変換するような対応を射関数といいます。一般に、圏 C から D への関手 F は、圏 C の射 f: A -> B を D の射 F f: F(A) -> F(B) に対応させます。

Option 関手の例で言うと、射 `f: A => B` を `F(f): Option[A] => Option[B]` に対応させる必要があります。この対応は、標準ライブラリにある `Option#map` メソッドによって実現されます：

```scala
def isEven: Int => Boolean = n => n % 2 == 0

Option(3).map(isEven)
// res2: Option[Boolean] = Some(value = false)
```

この射関数が満たすべき性質として、以下の2つがあります：

1. C の射 f, g の合成 g . f について F(f . g) = F(f) . F(g) が成り立つこと。
2. C の任意の対象 A の恒等射 idA について F(idA) = idFA が成り立つこと。ただし、idFA は圏 D の対象 F(A) についての恒等射です。

1つ目については、関手が射の合成を保存することを意味します。

```scala
def negate: Boolean => Boolean = b => !b

// F(f . g)
Option((negate compose isEven) (3))
// res3: Option[Boolean] = Some(value = true)

// F(f) . F(g)
Option(3).map(isEven).map(negate)
// res4: Option[Boolean] = Some(value = true)
```

2つ目については、関手が恒等射を保存することを意味します。

```scala
def identity[A]: A => A = a => a

// F(idA)
Option(identity(3))
// res5: Option[Int] = Some(value = 3)

// idFA
identity(Option(3))
// res6: Option[Int] = Some(value = 3)
```

このような2つの性質を関手性 (functor laws) と呼びます。

### 7.1.3 関手の定義

では、関手の定義を与えましょう。一般に、関手は以下のように定義されます。

---

圏 C から圏 D への関手 (functor) F とは、以下を満たす対応のことです。

- C の射 f: A -> B を D の射 F(f): F(A) -> F(B) に対応させること。
- C の射 f, g の合成 f . g について F(f . g) = F(f) . F(g) が成り立つこと。
- C の任意の対象 A の恒等射 idA について F(idA) = idFA が成り立つこと。ただし、idFA は D の対象 F(A) についての恒等射です。

---

先ほどみたように、2 番目と 3 番目は関手性を表します。

なお、圏 C と D は同じであってもよく、特に圏 C から圏 C への関手は自己関手 (endofunctor) と呼ばれます。Scala 圏における関手は全て、自己関手です。

## 7.2 プログラミングにおける関手

前節では、関手の定義を与えました。本節では、関手を Scala プログラミングにおける概念に限定して考えていきます。

### 7.2.1 Functor 型クラス

関手は Scala において、[Functor](https://github.com/taretmch/scala-category-training/blob/master/src/main/scala/data/Functor.scala) 型クラスとして実装できます。

```scala
trait Functor[F[_]] {
  def fmap[A, B](fa: F[A])(f: A => B): F[B]
}
```

`Functor` トレイトは型構築子 `F` を型パラメータとして持っており、射関数 `fmap: F[A] => (A => B) => F[B]` を持ちます。

### 7.2.2 Option 関手

Option 関手のインスタンスは、以下のように実装できます。

```scala
implicit val OptionFunctor: Functor[Option] = new Functor[Option] {
  def fmap[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
    case None    => None
    case Some(a) => Some(f(a))
  }
}
```

`fmap` は `Option#map` 関数と同じです。


```scala
import category.Implicits._

OptionFunctor.fmap(Option(3))(isEven)
// res7: Option[Boolean] = Some(value = false)
```

### 7.2.3 List 関手

### 7.2.4 Reader 関手

