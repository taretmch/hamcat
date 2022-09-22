package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given

class MonoidSpec extends Spec:

  describe("Int Monoid") {
    it("単位元を持つ") {
      assertUnit(summon[Monoid[Int]], 3)
    }
  }

  describe("String Monoid") {
    it("単位元を持つ") {
      assertUnit(summon[Monoid[String]], "hoge")
    }
  }

  describe("Option Monoid") {
    it("単位元を持つ") {
      assertUnit(summon[Monoid[Option[Int]]], Some(3))
      assertUnit(summon[Monoid[Option[Int]]], None)
      assertUnit(summon[Monoid[Option[String]]], Some("hoge"))
      assertUnit(summon[Monoid[Option[String]]], None)
    }
  }

  def assertUnit[M, A](monoid: Monoid[M], value: M) =
    assert(monoid.combine(value, monoid.empty) == value)
    assert(monoid.combine(monoid.empty, value) == value)
