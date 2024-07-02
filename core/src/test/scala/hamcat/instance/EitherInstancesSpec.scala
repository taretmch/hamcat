package hamcat.instance

import hamcat.{ Functor, Bifunctor }

import either.given

class EitherInstancesSpec extends munit.FunSuite:

  test("summon[Functor[Either[L, *]]] should compile") {
    summon[Functor[[X] =>> Either[String, X]]]
    summon[Functor[[X] =>> Either[Int, X]]]
    summon[Functor[[X] =>> Either[Boolean, X]]]
    case class Person(name: String, age: Int)
    summon[Functor[[X] =>> Either[Person, X]]]
  }

  test("Functor : fmap should apply the function to the value") {
    val functor                           = summon[Functor[[X] =>> Either[String, X]]]
    val actual                            = functor.fmap[Int, Boolean](_ > 0)(Right(10))
    val expected: Either[String, Boolean] = Right(true)
    assertEquals(actual, expected)
  }

  test("summon[Bifunctor[Either]] should compile") {
    summon[Bifunctor[Either]]
  }

  test("Bifunctor : bimap should apply functions to both sides") {
    val bifunctor                      = summon[Bifunctor[Either]]
    val actual                         =
      bifunctor.bimap[String, List[Int], Boolean, Int](_.nonEmpty, _.length)(
        Right(List(1, 2, 3, 4, 5))
      )
    val expected: Either[Boolean, Int] = Right(5)
    assertEquals(actual, expected)

    val actual2                         =
      bifunctor.bimap[String, List[Int], Boolean, Int](_.nonEmpty, _.length)(
        Left("hello")
      )
    val expected2: Either[Boolean, Int] = Left(true)
    assertEquals(actual2, expected2)
  }
