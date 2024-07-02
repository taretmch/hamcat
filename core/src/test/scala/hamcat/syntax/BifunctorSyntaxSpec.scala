package hamcat.syntax

import hamcat.instance.tuple.given
import hamcat.instance.either.given

import hamcat.syntax.bifunctor.*

class BifunctorSyntaxSpec extends munit.FunSuite:

  test("bimap : tuple") {
    val tuple    = (1, "hello")
    val expected = ("1", 5)
    assertEquals(tuple.bimap(_.toString, _.length), expected)
  }

  test("bimap : either") {
    val either: Either[Nothing, String] = Right("hello")
    val expected                        = Right(5)
    assertEquals(either.bimap(identity, _.length), expected)
  }
