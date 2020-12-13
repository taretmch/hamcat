package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class FunctorConstSpec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0

  def const: Const[String, Int] = Const("constant string")

  "Const functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(const.fmap(isEven compose increment) == const.fmap(increment).fmap(isEven))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(ConstFunctor.fmap(identity[Int])(const) == identity[Const[String, Int]](const))
  }
}
