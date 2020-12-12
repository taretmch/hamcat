package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._

class BifunctorTuple2Spec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  val strToLength: String => Int = _.length

  val tuple: (Int, String) = (3, "abcdefg")

  "Tuple2 bifunctor" should "射の合成を保存する" in {
    // bimap(h compose f)(k compose g) == bimap(h)(k) compose bimap(f)(g)
    assert(
      Tuple2Bifunctor.bimap(isEven compose increment)(isEven compose strToLength)(tuple)
        ==
      (Tuple2Bifunctor.bimap(isEven)(isEven) compose Tuple2Bifunctor.bimap(increment)(strToLength))(tuple)
    )
    assert(
      tuple.bimap(isEven compose increment)(isEven compose strToLength)
        ==
      tuple.bimap(increment)(strToLength).bimap(isEven)(isEven)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(Tuple2Bifunctor.bimap(identity[Int])(identity[String])(tuple) == identity[(Int, String)](tuple))
  }
}
