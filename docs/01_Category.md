# 1. 圏: 合成の本質

圏は**対象** (object) の集まりと**射** (arrows, morphism) の集まりからなります。

例えば、Scala を圏として考えてみると、対象は `Int`、 `String`、 `List[A]` などの型を表し，射は `f: Int -> String` のように関数を表します。

## 1.1 関数としての射

> 圏は**対象** (object) の集まりと**射** (arrows, morphism) の集まりからなる。

の説明から想像がつくように、圏は抽象的な概念です。高校の授業で、集合を「ものの集まり」と習ったのを懐かしく感じます。

射について、もう少し具体的に掘り下げてみましょう。入力に対して出力を返す関数は、射の例です。ここでは、関数の合成について見ます。

対象の例として、以下の3つのクラスを考えます。

```scala
scala> case class A(value: Int)
defined class A

scala> case class B(value: Int)
defined class B

scala> case class C(value: Int)
defined class C
```

これらの対象の間の射として、関数 `f:A => B`、`g: B => C` を考えます。 `f` は `A` の値をインクリメントしたものを `B` に変換する関数で、 `g` は `B` の値を2倍したものを `C` に変換する関数です。

```scala
scala> val f = (a: A) => B(a.value + 1)
f: A => B = $Lambda$6756/1477788485@40987409

scala> val g = (b: B) => C(b.value * 2)
g: B => C = $Lambda$6757/1606691516@2e7629b0

scala> val a = A(1)
a: A = A(1)

scala> val b = f(a)
b: B = B(2)

scala> val c1 = g(b)
c1: C = C(4)
```

`f` の返り値は `g` の入力としてそのまま渡すことができます。

```scala
scala> val c2 = g(f(a))
c2: C = C(4)
```

このような操作を関数の合成 (composition) といいます。数学的には

![composition of functions f and g](./images/latex/composition_of_function.png)

と書きます。scala では、関数の合成には `compose` を用います。

```scala
scala> g compose f
res2: A => C = scala.Function1$$Lambda$6733/1101289403@5799784c

scala> g (f(a)) == (g compose f) (a)
res1: Boolean = true
```

## 1.2 合成の性質

圏の射には、重要な2つの性質があります。それは

1. 結合律
2. 恒等射

です。

### 結合律

結合律の説明をするために、もう一つクラスと関数を導入します。

```scala
scala> case class D(value: Int)
defined class D

scala> val h = (c: C) => D(c.value * 10)
h: C => D = $Lambda$6758/1811383728@1df4cd10
```

結合律は、射 `f: A => B`, `g: B => C`, `h: C => D` に対して以下が成り立つことです。

```scala
h compose g compose f == h compose (g compose f) == (h compose g) compose f
```

要は `f` と `g` の合成と `h` を合成したものと、`g` と `h` の合成と `f` を合成したものは、同じになるという性質ですね。

実際に、確かめてみましょう。

```scala
scala> (h compose g compose f) (a)
res10: D = D(40)

scala> (h compose (g compose f)) (a)
res11: D = D(40)

scala> ((h compose g) compose f) (a)
res12: D = D(40)
```

確かに結合律は成り立っています。

### 恒等射

恒等射は、射 `f: A => B` について以下を満たすような射 `id[A]: A => A`, `id[B]: B => B` のことです。

```scala
f compose id[A] == f
id[B] compose f == f
```

要は、入力と出力が等しい関数です。

## 1.3 合成はプログラミングの本質である



## 1.4 チャレンジ

