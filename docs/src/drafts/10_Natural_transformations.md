- [10. 自然変換](#10-自然変換)
  - [10.1 自然変換とは](#101-自然変換とは)
    - [10.1.1 自然変換の例](#1011-自然変換の例)
    - [10.1.2 自然性](#1012-自然性)
    - [10.1.3 自然変換の定義](#1013-自然変換の定義)
    - [10.1.4 自然変換の例は、自然性を満たすか](#1014-自然変換の例は自然性を満たすか)
      - [headOption: List => Option](#headoption-list--option)
      - [length: List => Const](#length-list--const)
      - [flattenListOption: List[Option] => List](#flattenlistoption-listoption--list)
  - [10.2 関手圏](#102-関手圏)
    - [10.2.1 自然変換の合成](#1021-自然変換の合成)
    - [10.2.2 自然変換は結合律を満たすか](#1022-自然変換は結合律を満たすか)
    - [10.2.3 自然変換は単位律を満たすか](#1023-自然変換は単位律を満たすか)
  - [10.3 自然変換の性質](#103-自然変換の性質)
    - [10.3.1 自然同値](#1031-自然同値)
    - [10.3.2 共変関手の自然変換](#1032-共変関手の自然変換)
    - [10.3.3 水平合成と垂直合成](#1033-水平合成と垂直合成)

# 10. 自然変換

7章と8章で、関手が圏同士の対応であることを見ました。関手には List、Option、Writer、Reader などがあり、2つの圏の間には複数の関手が存在することを学びました。複数の関手が存在するとき、それらの等しさはどのように扱われるでしょうか？

ここでは、複数の関手の同等性について議論するために、自然変換を導入します。自然変換は、関手の性質を維持しながら関手間を対応させるものです。

本章ではまず、自然変換のイメージを掴むために、List 関手から Option 関手への自然変換など、具体例を見ていきます。

次に、自然変換の定義を与え、その定義と具体例とを照らし合わせます。

関手の変換である自然変換を導入すると、関手を対象として自然変換を射とするような圏を考えることができます。そのような圏は関手圏と呼ばれます。関手圏を導入するために、自然変換の合成、合成の結合律、単位律について議論します。

最後に、自然変換に関するいくつかの性質を見ていきます。関手の同等性を議論する際に使われる自然同値、共変関手の自然変換、水平合成と垂直合成について考えます。

## 10.1 自然変換とは

### 10.1.1 自然変換の例

まずは自然変換の具体例を見ていきましょう。

自然変換は関手間の変換なので、List 関手と Option 関手を変換させてみます。

List 関手から Option 関手への自然変換の例として、 `headOption`、`listToNone` などがあります。

```scala mdoc
def headOption[A]: List[A] => Option[A] = _.headOption

def listToNone[A](list: List[A]): Option[A] = None
```

Option 関手から List 関手への自然変換の例としては `toList`、`optionToNil` などがあります。

```scala mdoc
def toList[A]: Option[A] => List[A] = {
  case Some(a) => List(a)
  case None    => Nil
}

def optionToNil[A](option: Option[A]): List[A] = Nil
```

また、関手の合成もまた関手になるので、List[List[_]] 関手や List[Option[_]] 関手から List 関手への自然変換も考えることができます。

```scala mdoc
def flattenListList[A]: List[List[A]] => List[A] = _.flatten
def flattenListOption[A]: List[Option[A]] => List[A] = _.flatten
```

List 関手から Const 関手 (定数を保持する関手) への関数 `length` もまた、自然変換です。

```scala mdoc
import hamcat.data.Const

def length[A]: List[A] => Const[Int, A] = list => Const(list.length)
```

以上のように、Scala 圏の自己関手に関する自然変換は、ある型構築子からある型構築子への関数であることがわかります。

### 10.1.2 自然性

では、自然変換の定義を考えていきましょう。自然変換は関手の構造を保つ必要があるので、対象関数と射関数を変換する必要があります。

圏 `C` から圏 `D` への関手を `F` と `G` とし、`F` から `G` への変換 `alpha` を考えます。

関手の対象関数は、圏 `C` の対象 `A` を `F[A]` および `G[A]` に対応させるものでした。この2つの対象 `F[A]` と `G[A]` は圏 `D` の対象であるため、対象関数の変換は `D` の射 `alpha[A]` として定義されます：

```scala
def alpha[A]: F[A] => G[A]
```

ただし、`alpha[A]` は特定の対象 `A` に絞って変換しているので、自然変換 `alpha` の **A 成分**と呼ばれます。

次に射関数ですが、これは圏 `C` の射 `f: A => B` を圏 `D` の射 `fmapF(f): F[A] => F[B]` および `fmapG(f): G[A] => G[B]` に対応させるものでした。これらの対応は、自然変換の各成分 `alpha[A]: F[A] => G[A]` と `alpha[B]: F[B] => G[B]` を用いて以下のように与えられます：

```scala
fmapG(f) compose alpha[A]: F[A] => G[B]

alpha[B] compose fmapF(f): F[A] => G[B]
```

このように、射関数の変換には2通りの作り方があるため、整合性が保たれるようどちらの作り方でも結果が同じでなければいけません：

```scala
(fmapG(f) compose alpha[A])(fa) == (alpha[B] compose fmapF(f))(fa)
 ```

圏 `C` の任意の射 `f` についての上記の条件を、**自然性** (naturality condition) と呼びます。

関手間の変換が自然変換であるためには、自然性を満たさなければいけません。

### 10.1.3 自然変換の定義

一般に、自然変換の定義は、以下のように与えられます：

---

圏 `C` から圏 `D` への関手 `F` と `G` に対して、`F` から `G` への対応 `alpha` が**自然変換** (natural transformation) であるとは、`alpha` が以下の条件を満たすことをいいます。

1. `alpha` は、圏 `C` の任意の対象 `A` に対して `alpha[A]: F[A] => G[A]` を対応させること。
2. 圏 `C` の任意の射 `f: A => B` に対して `fmapG(f) compose alpha[A]` と `alpha[B] compose fmapF(f)` が等しくなること。

またこのとき、`alpha[A]` を自然変換 `alpha` の A 成分といいます。

---

定義の1つ目の条件は、関手の対象関数の変換です。

定義の2つ目の条件は、関手の射関数の変換に関する条件で、自然性と呼ばれるものです。

### 10.1.4 自然変換を表す型クラス

自然変換を表す型クラスとして、`FunctionK` 型クラスを導入します：

```scala
/** FunctionK: typeclass for mapping between first-order-kinded types */
trait FunctionK[F[_], G[_]] { self =>
  /** Apply method */
  def apply[A](fa: F[A]): G[A]
}
```

例によって、この型クラスを実装するだけでは自然変換かどうかはわかりません。そのため、FunctionK は単に first-order 型間の関数、すなわち `F[_]` から `G[_]` への関数の一般化となります。FunctionK の実装のうち、自然性を満たすような実装のみが自然変換です。

では先ほどの例から抜粋して、FunctionK のインスタンスを作ってみましょう。

`headOption` は以下のように定義されますが

```scala
def headOption[A]: List[A] => Option[A] = _.headOption
```

これの FunctionK のインスタンスを実装すると、以下のようになります。

```scala mdoc
import hamcat.arrow.FunctionK

val headOptionK: FunctionK[List, Option] = new FunctionK[List, Option] {
  def apply[A](fa: List[A]): Option[A] = fa.headOption
}
```

### 10.1.4 自然変換の例は、自然性を満たすか

自然変換の具体例と定義を見ましたので、具体例が実際に自然変換の定義を満たすかどうかについて考えていきます。

#### headOption: List => Option

headOption 関数は、List 関手から Option 関手への自然変換です。

実際、`List(1, 2, 3, 4, 5)` と `isEven` 関数に対して、自然性を満たします：

```scala mdoc
import hamcat.Implicits._

def isEven: Int => Boolean = _ % 2 == 0
val list = List(1, 2, 3, 4, 5)

// 自然性
val listToOption1 = (OptionFunctor.fmap(isEven) compose headOptionK[Int])(list)
val listToOption2 = (headOptionK[Boolean] _ compose ListFunctor.fmap(isEven))(list)
listToOption1 == listToOption2
```

#### length: List => Const

length 関数は、List 関手から Const 関手への自然変換です：

```scala
def length[A]: List[A] => Const[Int, A] = list => Const(list.length)
```

```scala mdoc
val lengthK = new FunctionK[List, Const[Int, ?]] {
  def apply[A](fa: List[A]): Const[Int, A] = Const(fa.length)
}
```

length もまた、`List(1, 2, 3, 4, 5)` と `isEven` 関数に対して、自然性を満たします：

```scala mdoc
// 自然性
val listToConst1 = (ConstFunctor.fmap(isEven) compose lengthK[Int])(list)
val listToConst2 = (lengthK[Boolean] _ compose ListFunctor.fmap(isEven))(list)
listToConst1 == listToConst2
```

#### flattenListOption: List[Option] => List

flattenListOption 関数は、List[Option] 関手から List 関手への自然変換です：

```scala
def flattenListOption[A]: List[Option[A]] => List[A] = _.flatten
```

```scala mdoc
type ListOption[A] = List[Option[A]]
val flattenListOptionK = new FunctionK[ListOption, List] {
  def apply[A](fa: List[Option[A]]): List[A] = fa.flatten
}
```

`List(Some(1), Some(2), None, Some(3))` と `isEven` 関数に対して、自然性を満たします：

```scala mdoc
val listOption = List(Some(1), Some(2), None, Some(3))
def fmapLO[A, B]: (A => B) => List[Option[A]] => List[Option[B]] = f => listA =>
  listA.fmap(_.fmap(f))

// 自然性
val listOptionToList1 = (ListFunctor.fmap(isEven) compose flattenListOptionK[Int])(listOption)
val listOptionToList2 = (flattenListOptionK[Boolean] _ compose fmapLO(isEven))(listOption)
listOptionToList1 == listOptionToList2
```

## 10.2 関手圏

自然変換は関手間の対応であるので、関手を対象として自然変換を射とするような圏を考えることができます。そのような圏は関手圏と呼ばれます。

---

一般に、圏 `C` から圏 `D` への関手を対象とし、その間の自然変換を射とする圏を圏 `C` から圏 `D` への**関手圏** (functor category) と呼び、`Fun(C, D)` と書きます。

---

### 10.2.1 自然変換の合成

関手圏が圏であるためには、射の合成と結合律、単位律が定義されている必要があります。まずは射の合成、すなわち自然変換の合成について考えてみましょう。

### 10.2.2 自然変換は結合律を満たすか

### 10.2.3 自然変換は単位律を満たすか

## 10.3 自然変換の性質

### 10.3.1 自然同値

### 10.3.2 共変関手の自然変換

TODO: Comming soon

### 10.3.3 水平合成と垂直合成

TODO: Comming soon