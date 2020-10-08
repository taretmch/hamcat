# 1. 圏: 合成の本質

圏は**対象** (object) の集まりと**射** (arrows, morphism) の集まりからなります。

例えば、Scala を圏として考えてみると、対象は `Int`、 `String`、 `List[A]` などの型を表し，射は `f: Int -> String` のように関数を表します。

## 1.1 関数としての射

> 圏は**対象** (object) の集まりと**射** (arrows, morphism) の集まりからなる。

の説明から想像がつくように、圏は抽象的な概念です。高校の授業で、集合を「ものの集まり」と習ったのを懐かしく感じます。

射について、もう少し具体的に掘り下げてみましょう。入力に対して出力を返す関数は，射の例です。ここでは、関数の合成について見ます。

例として、整数 `intNum` をインクリメントする関数 `increment` と整数 `intNum` を2倍する関数 `double` を考えます。

```scala
scala> val increment = (intNum: Int) => intNum + 1
increment: Int => Int = $Lambda$7298/268608341@127264cc

scala> val double = (intNum: Int) => intNum * 2
double: Int => Int = $Lambda$7300/463068654@22a1fba9
```

TODO: 異なる型間の関数の合成がよい

```scala
scala> double compose increment
res0: Int => Int = scala.Function1$$Lambda$7304/1123438292@4f75e6f6
```

関数 `f` は型 `A` の値を受け取り `B` の値を返し、関数 `g` は型 `B` の値を受け取り `C` の値を返します。

ここで、 `f` の戻り値を `g` に渡すことによって、これらの関数を合成することができます。

## 1.2 合成の性質

## 1.3 合成はプログラミングの本質である

## 1.4 チャレンジ

