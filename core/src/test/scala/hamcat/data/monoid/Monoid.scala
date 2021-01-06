package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class MonoidSpec extends AnyFlatSpec with Matchers {

  "IntMonoid" should "単位元を持つ" in {
    assert((3 |+| IntMonoid.empty) == 3)
    assert((IntMonoid.empty |+| 3) == 3)
  }

  "StringMonoid" should "単位元を持つ" in {
    assert(("Hoge" |+| StringMonoid.empty) == "Hoge")
    assert((StringMonoid.empty |+| "Hoge") == "Hoge")
  }

  "OptionMonoid" should "単位元を持つ" in {
    assert((Option(3) |+| OptionMonoid[Int].empty) == Option(3))
    assert((OptionMonoid[Int].empty |+| Option(3)) == Option(3))
    assert((Option("Hoge") |+| OptionMonoid[String].empty) == Option("Hoge"))
    assert((OptionMonoid[String].empty |+| Option("Hoge")) == Option("Hoge"))
  }
}
