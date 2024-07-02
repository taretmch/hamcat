package hamcat.data

import hamcat.Monoid

class WriterSpec extends munit.FunSuite:

  test("apply") {
    val w = Writer("hello", 42)
    assertEquals(w.run, ("hello", 42))
    assertEquals(w.log, "hello")
    assertEquals(w.value, 42)

    val w2 = Writer(("hello", 42))
    assertEquals(w2.run, ("hello", 42))
    assertEquals(w.log, "hello")
    assertEquals(w.value, 42)
  }

  test("pure should need a Monoid instance") {
    import hamcat.instance.string.given
    import hamcat.instance.int.given

    val w = Writer.pure[String, Int](42)
    val expected = Writer(summon[Monoid[String]].empty, 42)
    assertEquals(w, expected)

    val w2 = Writer.pure[Int, String]("hello")
    val expected2 = Writer(summon[Monoid[Int]].empty, "hello")
    assertEquals(w2, expected2)
  }

  test("fmap should apply the function to the value") {
    import hamcat.instance.string.given

    val w = Writer("hello", 10)
    val mapped = w.fmap(_ + 1)
    val expected = Writer("hello", 11)
    assertEquals(mapped, expected)
  }

  test("flatMap should apply the function to the value") {
    import hamcat.instance.string.given

    val w = Writer("hello", 10)
    val mapped = w.flatMap(a => Writer(" world", a + 1))
    val expected = Writer("hello world", 11)
    assertEquals(mapped, expected)
  }

  test(">=> should compose two morphisms") {
    import hamcat.instance.string.given

    val m1 = (a: Int) => Writer("hello", a + 1)
    val m2 = (b: Int) => Writer(" world", b * 2)
    val composed = m1 >=> m2
    val expected = (a: Int) => Writer("hello world", (a + 1) * 2)
    assertEquals(composed(10), expected(10))
  }
