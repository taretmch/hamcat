package hamcat.instance

import hamcat.*

import int.given

class IntInstancesSpec extends munit.FunSuite:

  test("Semigroup : summon") {
    summon[Semigroup[Int]]
  }

  test("Semigroup : combine") {
    val semigroup = summon[Semigroup[Int]]
    assertEquals(semigroup.combine(1, 5), 6)
  }

  test("Monoid : summon") {
    summon[Monoid[Int]]
  }

  test("Monoid : combine") {
    val monoid = summon[Monoid[Int]]
    assertEquals(monoid.combine(1, 5), 6)
  }

  test("Monoid : empty") {
    val monoid = summon[Monoid[Int]]
    assertEquals(monoid.empty, 0)
  }
