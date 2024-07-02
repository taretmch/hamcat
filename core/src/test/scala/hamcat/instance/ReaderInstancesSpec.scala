package hamcat.instance

import hamcat.*
import hamcat.data.Reader

import reader.given

class ReaderInstancesSpec extends munit.FunSuite:

  test("Functor : summon") {
    summon[Functor[[X] =>> Reader[Int, X]]]
    summon[Functor[[X] =>> Reader[String, X]]]
    summon[Functor[[X] =>> Reader[Boolean, X]]]
    case class Person(name: String, age: Int)
    summon[Functor[[X] =>> Reader[Person, X]]]
  }

  test("Functor : fmap") {
    val functor = summon[Functor[[X] =>> Reader[String, X]]]
    val r = Reader((s: String) => s.length)
    val mapped = functor.fmap[Int, Int](_ + 1)(r)
    assertEquals(mapped.run("hello"), 6)
  }

  test("Contravariant : summon") {
    summon[Contravariant[[X] =>> Reader[X, Int]]]
    summon[Contravariant[[X] =>> Reader[X, String]]]
    summon[Contravariant[[X] =>> Reader[X, Boolean]]]
    case class Person(name: String, age: Int)
    summon[Contravariant[[X] =>> Reader[X, Person]]]
  }

  test("Contravariant : contramap") {
    val contravariant = summon[Contravariant[[X] =>> Reader[X, Int]]]
    val r = Reader((s: String) => s.length)
    val contramapped = contravariant.contramap[String, Int](_.toString)(r)
    assertEquals(contramapped.run(12345), 5)
  }

  test("Profunctor : summon") {
    summon[Profunctor[Reader]]
  }

  test("Profunctor : dimap") {
    val profunctor = summon[Profunctor[Reader]]
    val r = Reader((s: String) => s.length)
    val dimapped = profunctor.dimap[String, Int, Int, Boolean](_.toString, _ > 0)(r)
    assertEquals(dimapped.run(12345), true)
  }
