package hamcat.instance

import hamcat.Functor

import hamcat.data.Writer

import writer.given

class WriterInstancesSpec extends munit.FunSuite:

  test("Functor : summon") {
    import hamcat.instance.string.given

    summon[Functor[[X] =>> Writer[String, X]]]
  }

  test("Functor : fmap") {
    import hamcat.instance.string.given
    val functor  = summon[Functor[[X] =>> Writer[String, X]]]
    val mapped   =
      functor.fmap[List[Int], Int](_.sum)(Writer("hello", List(1, 2, 3)))
    val expected = Writer("hello", 6)
    assertEquals(mapped, expected)
  }
