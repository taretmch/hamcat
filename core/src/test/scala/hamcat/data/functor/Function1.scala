package category.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class FunctorFunction1Spec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0
  def identity[A]: A   => A       = a => a

  def func: Function1[String, Int] = _.length
  val str = "abcdefg"

  "Reader functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(func.fmap(isEven compose increment)(str) == func.fmap(increment).fmap(isEven)(str))
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(Function1Functor.fmap(identity[Int])(func)(str) == identity[Function1[String, Int]](func)(str))
  }
}
