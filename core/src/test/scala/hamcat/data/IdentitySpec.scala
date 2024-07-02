package hamcat.data

class IdentitySpec extends munit.FunSuite:

  test("apply") {
    val i = Identity(42)
    assertEquals(i.value, 42)
  }

  test("fmap should apply the function to the value") {
    val i = Identity(10)
    val mapped = i.fmap(_ + 1)
    assertEquals(mapped.value, 11)
  }

  test("fmap should update type parameter") {
    val i = Identity("hello")
    val mapped: Identity[Boolean] = i.fmap(_.length > 0)
    assertEquals(mapped.value, true)
  }

  test("using Identity with different types") {
    val intIdentity = Identity(5)
    val stringIdentity = Identity("hello")
    val boolIdentity = Identity(true)

    assertEquals(intIdentity.value, 5)
    assertEquals(stringIdentity.value, "hello")
    assertEquals(boolIdentity.value, true)
  }

  test("using Identity with custom case class") {
    case class Person(name: String, age: Int)
    val person = Person("Alice", 30)
    val personIdentity = Identity(person)
    assertEquals(personIdentity.value, person)
  }

  test("using null with Identity") {
    val nullIdentity = Identity(null)
    assertEquals(nullIdentity.value, null)
  }
