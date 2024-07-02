package hamcat.instance

import hamcat.Functor

import hamcat.data.Const

class ConstInstancesSpec extends munit.FunSuite:

  import const.given

  test("summon[Functor[Const[A]]] should compile") {
    summon[Functor[[X] =>> Const[Int, X]]]
    summon[Functor[[X] =>> Const[String, X]]]
    summon[Functor[[X] =>> Const[Boolean, X]]]
    case class Person(name: String, age: Int)
    summon[Functor[[X] =>> Const[Person, X]]]
  }

  test("fmap should apply the function to the value") {
    val functor  = summon[Functor[[X] =>> Const[Int, X]]]
    val c        = Const[Int, String](10)
    val actual   = functor.fmap[String, Int](_.length)(c)
    val expected = c.fmap(_.length)
    assertEquals(actual, expected)
  }
