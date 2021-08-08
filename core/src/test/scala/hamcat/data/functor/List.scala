package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.implicits._

class FunctorListSpec extends AnyFlatSpec with Matchers {
  val functor:     Functor[List]  = Functor[List]
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a

  val list: List[Int] = List(1, 2, 3, 4, 5)
  val nil: List[Int] = Nil

  "List functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(
      functor.fmap(isEven compose increment)(list)
        ==
      (functor.fmap(isEven) compose functor.fmap(increment))(list)
    )
    assert(list.fmap(isEven compose increment) == list.fmap(increment).fmap(isEven))

    assert(
      functor.fmap(isEven compose increment)(nil)
        ==
      (functor.fmap(isEven) compose functor.fmap(increment))(nil)
    )
    assert(nil.fmap(isEven compose increment) == nil.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(functor.fmap(identity[Int])(list) == identity[List[Int]](list))
    assert(functor.fmap(identity[Int])(nil) == identity[List[Int]](nil))
  }
}
