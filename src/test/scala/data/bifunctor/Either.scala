package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._

class BifunctorEitherSpec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  val strToLength: String => Int = _.length

  val left: Either[Int, String] = Left(3)
  val right: Either[Int, String] = Right("abcdefg")

  "Either bifunctor" should "射の合成を保存する" in {
    // bimap(h compose f)(k compose g) == bimap(h)(k) compose bimap(f)(g)
    // Case: Right
    assert(
      EitherBifunctor.bimap(isEven compose increment)(isEven compose strToLength)(left)
        ==
      (EitherBifunctor.bimap(isEven)(isEven) compose EitherBifunctor.bimap(increment)(strToLength))(left)
    )
    assert(
      left.bimap(isEven compose increment)(isEven compose strToLength)
        ==
      left.bimap(increment)(strToLength).bimap(isEven)(isEven)
    )

    // Case: Left
    assert(
      EitherBifunctor.bimap(isEven compose increment)(isEven compose strToLength)(right)
        ==
      (EitherBifunctor.bimap(isEven)(isEven) compose EitherBifunctor.bimap(increment)(strToLength))(right)
    )
    assert(
      right.bimap(isEven compose increment)(isEven compose strToLength)
        ==
      right.bimap(increment)(strToLength).bimap(isEven)(isEven)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(EitherBifunctor.bimap(identity[Int])(identity[String])(left) == identity[Either[Int, String]](left))
    assert(EitherBifunctor.bimap(identity[Int])(identity[String])(right) == identity[Either[Int, String]](right))
  }
}
