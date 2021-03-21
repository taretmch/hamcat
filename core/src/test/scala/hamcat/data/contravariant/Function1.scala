package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class ContravariantFunction1Spec extends AnyFlatSpec with Matchers {
  val increment: Int => Int     = n => n + 1
  val isEven:    Int => Boolean = _ % 2 == 0
  val boolToInt: Boolean => Int = {
    case false => 0
    case tru   => 1
  }

  def func: Function1[Int, Boolean] = _ % 2 == 1

  "Function1 contravariant functor" should "射の合成を保存する" in {
    // contramap(g compose f) == contramap(f) compose contramap(g)
    assert(
      readerContravariant.contramap(boolToInt compose isEven)(func)(3)
        ==
      (readerContravariant[Boolean].contramap(isEven) compose readerContravariant[Boolean].contramap(boolToInt))(func)(3)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    // contramacontramap(identity[A]) == identity[F[A]]
    assert(readerContravariant.contramap(identity[Int])(func)(3) == identity[Int => Boolean](func)(3))
    assert(readerContravariant.contramap(identity[Int])(func)(4) == identity[Int => Boolean](func)(4))
  }
}