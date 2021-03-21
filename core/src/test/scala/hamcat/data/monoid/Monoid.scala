package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class MonoidSpec extends AnyFlatSpec with Matchers {

  "IntMonoid" should "単位元を持つ" in {
    assert((3 |+| intMonoid.empty) == 3)
    assert((intMonoid.empty |+| 3) == 3)
  }

  "StringMonoid" should "単位元を持つ" in {
    assert(("Hoge" |+| stringMonoid.empty) == "Hoge")
    assert((stringMonoid.empty |+| "Hoge") == "Hoge")
  }

  "OptionMonoid" should "単位元を持つ" in {
    assert((Option(3) |+| optionMonoid[Int].empty) == Option(3))
    assert((optionMonoid[Int].empty |+| Option(3)) == Option(3))
    assert((Option("Hoge") |+| optionMonoid[String].empty) == Option("Hoge"))
    assert((optionMonoid[String].empty |+| Option("Hoge")) == Option("Hoge"))
  }
}
