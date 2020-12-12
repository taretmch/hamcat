package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._
import category.data.Writer
import category.data.Writer._

class WriterSpec extends AnyFlatSpec with Matchers {

  def increment(n: Int): Writer[String, Int] =
    Writer(("increment ", n + 1))

  def isEven(n: Int): Writer[String, Boolean] =
    Writer(("isEven ", n % 2 == 0))

  def negate(b: Boolean): Writer[String, Boolean] =
    Writer(("negate ", !b))

  "Writer 圏" should "結合律を満たす" in {
    assert(
      ((increment _ >=> isEven) >=> negate)(3)
      == (increment _ >=> (isEven _ >=> negate))(3)
    )
  }

  "Writer 圏" should "単位律を満たす" in {
    assert((Writer.pure[String, Int] _ >=> isEven)                      (3) == isEven(3))
    assert((isEven _                   >=> Writer.pure[String, Boolean])(3) == isEven(3))
  }
}
