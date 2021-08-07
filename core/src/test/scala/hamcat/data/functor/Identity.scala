package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.implicits._
import hamcat.implicitConversions._

class FunctorIdentitySpec extends AnyFlatSpec with Matchers {
  val instance = Functor[Identity]
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a

  val oddId: Identity[Int]  = 3
  val evenId: Identity[Int] = 4

  "Identity functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(
      instance.fmap(isEven compose increment)(oddId)
        ==
      (instance.fmap(isEven) compose instance.fmap(increment))(oddId)
    )
    assert(oddId.fmap(isEven compose increment) == oddId.fmap(increment).fmap(isEven))

    assert(
      instance.fmap(isEven compose increment)(evenId)
        ==
      (instance.fmap(isEven) compose instance.fmap(increment))(evenId)
    )
    assert(evenId.fmap(isEven compose increment) == evenId.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(instance.fmap(identity[Int])(oddId) == identity[Identity[Int]](oddId))
    assert(instance.fmap(identity[Int])(evenId) == identity[Identity[Int]](evenId))
  }
}
