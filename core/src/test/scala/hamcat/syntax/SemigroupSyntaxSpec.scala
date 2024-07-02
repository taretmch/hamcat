package hamcat.syntax

import hamcat.instance.int.given
import hamcat.instance.string.given
import hamcat.instance.option.given

import hamcat.syntax.semigroup.|+|

class SemigroupSyntaxSpec extends munit.FunSuite:

  test("|+| : Int") {
    val a = 1
    val b = 2
    val expected = 3
    assertEquals(a |+| b, expected)
  }

  test("|+| : Option[Int]") {
    val a = Option(1)
    val b = Option(2)
    val expected = Option(3)
    assertEquals(a |+| b, expected)
  }

  test("|+| : String") {
    val a = "hello"
    val b = " world"
    val expected = "hello world"
    assertEquals(a |+| b, expected)
  }
