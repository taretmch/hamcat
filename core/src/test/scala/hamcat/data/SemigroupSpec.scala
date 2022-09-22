package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given
import hamcat.syntax.Implicits.*

class SemigroupSpec extends Spec:

  describe("IntSemigroup") {
    it("結合律を満たす") {
      assert(((10 |+| 3) |+| 5) == (10 |+| (3 |+| 5)))
    }
  }

  describe("StringSemigroup") {
    it("結合律を満たす") {
      assert((("Hoge " |+| "Foo ") |+| "Bar ") == ("Hoge " |+| ("Foo " |+| "Bar ")))
    }
  }

  describe("OptionSemigroup") {
    it("結合律を満たす") {
      assert(
        ((Option(10) |+| Option(3)) |+| Option(5)) == (Option(10) |+| (Option(3) |+| Option(5))))
      assert(((Option("Hoge ") |+| Option("Foo ")) |+| Option("Bar "))
        == (Option("Hoge ") |+| (Option("Foo ") |+| Option("Bar ")))
      )
    }
  }
