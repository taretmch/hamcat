package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.implicits._

class MonoidSpec extends AnyFlatSpec with Matchers {

  object instances {
    val int       = Monoid[Int]
    val string    = Monoid[String]
    val optInt    = Monoid[Option[Int]]
    val optString = Monoid[Option[String]]
  }

  "IntMonoid" should "単位元を持つ" in {
    assert((3 |+| instances.int.empty) == 3)
    assert((instances.int.empty |+| 3) == 3)
  }

  "StringMonoid" should "単位元を持つ" in {
    assert(("Hoge" |+| instances.string.empty) == "Hoge")
    assert((instances.string.empty |+| "Hoge") == "Hoge")
  }

  "OptionMonoid" should "単位元を持つ" in {
    assert((Option(3) |+| instances.optInt.empty) == Option(3))
    assert((instances.optInt.empty |+| Option(3)) == Option(3))
    assert((Option("Hoge") |+| instances.optString.empty) == Option("Hoge"))
    assert((instances.optString.empty |+| Option("Hoge")) == Option("Hoge"))
  }
}
