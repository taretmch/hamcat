package hamcat.instance

import hamcat.*

import list.given

class ListInstancesSpec extends munit.FunSuite:

  test("Functor : summon") {
    summon[Functor[List]]
  }

  test("Functor : fmap should apply the function to the value") {
    val functor  = summon[Functor[List]]
    val actual   =
      functor.fmap[String, Int](_.length)(List("hello, ", "world", "!"))
    val expected = List(7, 5, 1)
    assertEquals(actual, expected)
  }
