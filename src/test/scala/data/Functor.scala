package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._
import category.universal.example.{ List, Nil, Cons }

class FunctorSpec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a

  val none: Option[Int] = None
  val nil:  List[Int]   = Nil

  "Option functor" should "射の合成を保存する" in {
    // F(f . g) == F(g) . F(f)
    // Case: Some(1)
    assert(Option(1).fmap(isEven compose increment) == Option(1).fmap(increment).fmap(isEven))

    // Case: None
    assert(none.fmap(isEven compose increment) == none.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // F(idA) == idFA
    // Case: Some(1)
    assert(Option(1).fmap(identity) == identity(Option(1)))

    // Case: None
    assert(none.fmap(identity) == identity(none))
  }

  "List functor" should "射の合成を保存する" in {
    assert(List(1).fmap(isEven compose increment) == List(1).fmap(increment).fmap(isEven))

    assert(nil.fmap(isEven compose increment) == nil.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    assert(List(1).fmap(identity) == identity(List(1)))

    assert(nil.fmap(identity) == identity(nil))
  }
}
