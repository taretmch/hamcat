package hamcat.arrow

class FunctionKSpec extends munit.FunSuite:

  val headOptionK = new FunctionK[List, Option]:
    def apply[A](fa: List[A]): Option[A] = fa.headOption

  val flattenK = new FunctionK[[X] =>> List[List[X]], List]:
    def apply[A](fa: List[List[A]]): List[A] = fa.flatten

  test("apply : headOptionK (List ~> Option)") {
    assertEquals(headOptionK(List(1, 2, 3)), Some(1))
    assertEquals(headOptionK(Nil), None)
  }

  test("apply : flattenK (List[List] ~> List)") {
    assertEquals(flattenK(List(List(1, 2), Nil, List(3, 4))), List(1, 2, 3, 4))
    assertEquals(flattenK(Nil), Nil)
  }

  test("compose : headOptionK and flattenK") {
    val headOptionAndFlattenK: FunctionK[[X] =>> List[List[X]], Option] = headOptionK.compose(flattenK)

    assertEquals(headOptionAndFlattenK(List(List(1, 2), Nil, List(3, 4))), Some(1))
    assertEquals(headOptionAndFlattenK(Nil), None)
  }

  test("andThen : flattenK and headOptionK") {
    val flattenAndHeadOptionK: FunctionK[[X] =>> List[List[X]], Option] = flattenK.andThen(headOptionK)

    assertEquals(flattenAndHeadOptionK(List(List(1, 2), Nil, List(3, 4))), Some(1))
    assertEquals(flattenAndHeadOptionK(Nil), None)
  }

  test("identityK : headOptionK") {
    import FunctionK.identityK
    val headOptionIdentityK: FunctionK[List, Option] = identityK.compose(headOptionK)
    val headOptionIdentityK2: FunctionK[List, Option] = headOptionK.compose(identityK)

    val expected = Some("a")
    assertEquals(headOptionK(List("a", "b", "c")), expected)
    assertEquals(headOptionIdentityK(List("a", "b", "c")), expected)
    assertEquals(headOptionIdentityK2(List("a", "b", "c")), expected)
  }

  test("~> : headOptionK") {
    val headOptionK2: List ~> Option = headOptionK
    val expected = Some("a")
    assertEquals(headOptionK2(List("a", "b", "c")), expected)
  }
