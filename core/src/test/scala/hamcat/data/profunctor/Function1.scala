package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class ProfunctorReaderSpec extends AnyFlatSpec with Matchers {
  val strToLength: String => Int  = _.length
  val isEven:      Int => Boolean = _ % 2 == 0
  val increment:   Int => Int     = _ + 1
  val boolToInt:   Boolean => Int = {
    case false => 0
    case true => 1
  }

  val func: Int => String = {
    case n if n < 10  => "less than 10"
    case n if n < 30  => "less than 30"
    case n if n >= 50 => "greater than 50"
    case _            => "hoge"
  }

  "Function1 profunctor" should "射の合成を保存する" in {
    // bimap(g compose f) == bimap(g) compose bimap(f)
    assert(
      readerProfunctor.bimap(boolToInt compose isEven)(isEven compose strToLength)(func)(44)
        ==
      (readerProfunctor.bimap(isEven)(isEven) compose readerProfunctor.bimap(boolToInt)(strToLength))(func)(44)
    )
    assert(
      func.bimap(boolToInt compose isEven)(isEven compose strToLength)(44)
        ==
      func.bimap(boolToInt)(strToLength).bimap(isEven)(isEven)(44)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    // bimap(identity[A]) == identity[F[A]]
    assert(readerProfunctor.bimap(identity[Int])(identity[String])(func)(44) == identity[Function1[Int, String]](func)(44))
  }
}