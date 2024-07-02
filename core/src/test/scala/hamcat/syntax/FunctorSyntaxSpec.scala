package hamcat.syntax

import hamcat.instance.list.given
import hamcat.instance.option.given

import hamcat.syntax.functor.*

class FunctorSyntaxSpec extends munit.FunSuite:

  test("fmap : List") {
    val list     = List(1, 2, 3)
    val expected = List(2, 3, 4)
    assertEquals(list.fmap(_ + 1), expected)
  }

  test("fmap : Option") {
    val option   = Option(42)
    val expected = Option(43)
    assertEquals(option.fmap(_ + 1), expected)
  }
