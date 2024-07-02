package hamcat.instance

import hamcat.*

import string.given

class StringInstancesSpec extends munit.FunSuite:

  test("Semigroup : summon") {
    summon[Semigroup[String]]
  }

  test("Semigroup : combine") {
    val semigroup = summon[Semigroup[String]]
    assertEquals(semigroup.combine("hello", " world"), "hello world")
  }

  test("Monoid : summon") {
    summon[Monoid[String]]
  }

  test("Monoid : combine") {
    val monoid = summon[Monoid[String]]
    assertEquals(monoid.combine("hello", " world"), "hello world")
  }

  test("Monoid : empty") {
    val monoid = summon[Monoid[String]]
    assertEquals(monoid.empty, "")
  }
