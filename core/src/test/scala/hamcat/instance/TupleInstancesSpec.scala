package hamcat.instance

import hamcat.Bifunctor

import tuple.given

class TupleInstancesSpec extends munit.FunSuite:

  test("Bifunctor : summon") {
    summon[Bifunctor[Tuple2]]
  }

  test("Bifunctor : bimap") {
    val b        = summon[Bifunctor[Tuple2]]
    val f        = (a: Int) => a.toString
    val g        = (b: String) => b.length
    val actual   = b.bimap(f, g)((123, "hello"))
    val expected = ("123", 5)
    assertEquals(actual, expected)
  }
