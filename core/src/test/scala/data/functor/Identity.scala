package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import category.Implicits._

class FunctorIdentitySpec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a

  val oddId: Id[Int]  = 3
  val evenId: Id[Int] = 4

  "Identity functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(
      IdentityFunctor.fmap(isEven compose increment)(oddId)
        ==
      (IdentityFunctor.fmap(isEven) compose IdentityFunctor.fmap(increment))(oddId)
    )
    assert(oddId.fmap(isEven compose increment) == oddId.fmap(increment).fmap(isEven))

    assert(
      IdentityFunctor.fmap(isEven compose increment)(evenId)
        ==
      (IdentityFunctor.fmap(isEven) compose IdentityFunctor.fmap(increment))(evenId)
    )
    assert(evenId.fmap(isEven compose increment) == evenId.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(IdentityFunctor.fmap(identity[Int])(oddId) == identity[Id[Int]](oddId))
    assert(IdentityFunctor.fmap(identity[Int])(evenId) == identity[Id[Int]](evenId))
  }
}
