package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.implicits._

class FunctorOptionSpec extends AnyFlatSpec with Matchers {
  val functor = Functor[Option]
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a
  val none: Option[Int] = None

  "Option functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    // Case: Some(1)
    assert(
      functor.fmap(isEven compose increment)(Option(1))
        ==
      (functor.fmap(isEven) compose functor.fmap(increment))(Option(1))
    )
    assert(Option(1).fmap(isEven compose increment) == Option(1).fmap(increment).fmap(isEven))

    // Case: None
    assert(
      functor.fmap(isEven compose increment)(none)
        ==
      (functor.fmap(isEven) compose functor.fmap(increment))(none)
    )
    assert(none.fmap(isEven compose increment) == none.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    // Case: Some(1)
    assert(functor.fmap(identity[Int])(Option(1)) == identity[Option[Int]](Option(1)))

    // Case: None
    assert(functor.fmap(identity[Int])(none) == identity[Option[Int]](none))
  }
}
