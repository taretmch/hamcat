package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._

class SemigroupSpec extends AnyFlatSpec with Matchers {

  "IntSemigroup" should "結合律を満たす" in {
    assert(((10 |+| 3) |+| 5) == (10 |+| (3 |+| 5)))
  }

  "StringSemigroup" should "結合律を満たす" in {
    assert((("Hoge " |+| "Foo ") |+| "Bar ") == ("Hoge " |+| ("Foo " |+| "Bar ")))
  }

  "OptionSemigroup" should "結合律を満たす" in {
    assert(
      ((Option(10) |+| Option(3)) |+| Option(5)) == (Option(10) |+| (Option(3) |+| Option(5))))
    assert(((Option("Hoge ") |+| Option("Foo ")) |+| Option("Bar "))
      == (Option("Hoge ") |+| (Option("Foo ") |+| Option("Bar ")))
    )
  }
}
