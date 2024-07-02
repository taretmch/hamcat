package hamcat.instance

import hamcat.*

import option.given

class OptionInstancesSpec extends munit.FunSuite:

  test("Semigroup : summon") {
    import int.given
    import string.given

    summon[Semigroup[Option[Int]]]
    summon[Semigroup[Option[String]]]
  }

  test("Semigroup : combine should combine two options") {
    import int.given

    val semigroup = summon[Semigroup[Option[Int]]]
    val v1 = Some(5)
    val v2 = Some(10)
    val v3 = None
    val combined1 = semigroup.combine(v1, v2)
    val combined2 = semigroup.combine(v1, v3)
    val combined3 = semigroup.combine(v3, v2)
    val combined4 = semigroup.combine(v3, v3)
    assertEquals(combined1, Some(15))
    assertEquals(combined2, Some(5))
    assertEquals(combined3, Some(10))
    assertEquals(combined4, None)
  }

  test("Monoid : summon") {
    import int.given
    import string.given

    summon[Monoid[Option[Int]]]
    summon[Monoid[Option[String]]]
  }

  test("Monoid : empty should return an empty option") {
    import int.given

    val monoid = summon[Monoid[Option[Int]]]
    val empty = monoid.empty
    assertEquals(empty, None)
  }

  test("Functor : summon") {
    summon[Functor[Option]]
  }

  test("Functor : fmap should apply the function to the value") {
    val functor = summon[Functor[Option]]
    val f = (a: Int) => a + 1
    val mapped = functor.fmap(f)(Some(10))
    assertEquals(mapped, Some(11))
  }
