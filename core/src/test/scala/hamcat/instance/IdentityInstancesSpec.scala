package hamcat.instance

import hamcat.Functor
import hamcat.data.Identity

import identity.given

class IdentityInstancesSpec extends munit.FunSuite:

  test("summon[Functor[Identity]] should compile") {
    summon[Functor[Identity]]
  }

  test("Functor : fmap should apply the function to the value") {
    val functor = summon[Functor[Identity]]
    val actual = functor.fmap[String, Int](_.length)(Identity("hello"))
    val expected = Identity("hello").fmap(_.length)
    assertEquals(actual, expected)
  }
