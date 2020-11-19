package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._
import category.universal.example.{ List, Nil, Cons }

class FunctorSpec extends AnyFlatSpec with Matchers {

  "Option functor" should "関手性を満たす" in {
    val increment: Int => Int     = n => n + 1
    val isEven:    Int => Boolean = n => n % 2 == 0

    assert(
      Option(1).fmap(isEven compose increment)
      == Option(1).fmap(increment).fmap(isEven)
    )

    val none: Option[Int] = None
    assert(
      none.fmap(isEven compose increment)
      == none.fmap(increment).fmap(isEven)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    def identity[A](a: A) = a
    assert(
      Option(1).fmap(identity)
      == Option(1)
    )

    val none: Option[Int] = None

    assert(none.fmap(identity) == none)
  }

  "List functor" should "関手性を満たす" in {
    val increment: Int => Int     = n => n + 1
    val isEven:    Int => Boolean = n => n % 2 == 0

    assert(
      List(1).fmap(isEven compose increment)
      == List(1).fmap(increment).fmap(isEven)
    )

    val nil: List[Int] = Nil
    assert(
      nil.fmap(isEven compose increment)
      == nil.fmap(increment).fmap(isEven)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    def identity[A](a: A) = a
    assert(List(1).fmap(identity) == List(1))

    val nil: List[Int] = Nil
    assert(nil.fmap(identity) == nil)
  }
}
