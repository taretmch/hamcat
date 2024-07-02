package hamcat.data

class ConstSpec extends munit.FunSuite:

  test("apply") {
    val c = Const(42)
    assertEquals(c.value, 42)
  }

  test("fmap should not change the original value") {
    val c      = Const[Int, String](10)
    val mapped = c.fmap(_.length)
    assertEquals(mapped.value, 10)
  }

  test("fmap should update type parameter") {
    val c                              = Const[String, Int]("hello")
    val mapped: Const[String, Boolean] = c.fmap(_ > 0)
    assertEquals(mapped.value, "hello")
  }

  test("using Const with different types") {
    val intConst    = Const[Int, String](5)
    val stringConst = Const[String, Int]("hello")
    val boolConst   = Const[Boolean, Double](true)

    assertEquals(intConst.value, 5)
    assertEquals(stringConst.value, "hello")
    assertEquals(boolConst.value, true)
  }

  test("using Const with custom case class") {
    case class Person(name: String, age: Int)
    val person      = Person("Alice", 30)
    val personConst = Const[Person, String](person)
    assertEquals(personConst.value, person)
  }

  test("using null with Const") {
    val nullConst = Const[Null, String](null)
    assertEquals(nullConst.value, null)
  }
