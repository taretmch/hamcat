package hamcat.data

class ReaderSpec extends munit.FunSuite:

  test("apply function") {
    val r = Reader((s: String) => s.length)
    assertEquals(r.run("hello"), 5)
  }

  test("fmap should apply the function to the value") {
    val r      = Reader((s: String) => s.length)
    val mapped = r.fmap(_ + 1)
    assertEquals(mapped.run("hello"), 6)
  }

  test("fmap should update type parameter") {
    val r                               = Reader((s: String) => s.length)
    val mapped: Reader[String, Boolean] = r.fmap(_ > 0)
    assertEquals(mapped.run("hello"), true)
  }

  test("contramap should apply the function to the input") {
    val r            = Reader((s: String) => s.length)
    val contramapped = r.contramap((s: Int) => s.toString)
    assertEquals(contramapped.run(12345), 5)
  }
