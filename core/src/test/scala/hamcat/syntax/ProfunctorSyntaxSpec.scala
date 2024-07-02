package hamcat.syntax

import hamcat.Profunctor

import hamcat.data.Reader

import hamcat.instance.reader.given

import profunctor.*

class ProfuctorSyntaxSpec extends munit.FunSuite:

  test("summon Profunctor") {
    summon[Profunctor[Reader]]
  }

  test("dimap") {
    val reader   = Reader((x: String) => x.toIntOption)
    val dimapped = reader.dimap[Int, Int](_.toString, _.getOrElse(0))
    assertEquals(dimapped.run(123), 123)
  }
