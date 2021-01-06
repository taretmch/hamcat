package hamcat.data

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.Implicits._

class FunctorWriterSpec extends AnyFlatSpec with Matchers {
  val increment:   Int => Int     = n => n + 1
  val isEven:      Int => Boolean = n => n % 2 == 0

  val writer: Writer[String, Int] = Writer("This is a log", 3)

  "Writer functor" should "射の合成を保存する" in {
    // fmap(g compose f) == fmap(g) compose fmap(f)
    assert(
      writer.fmap(isEven compose increment) == writer.fmap(increment).fmap(isEven)
    )
  }

  it should "恒等射を恒等射へ写す" in {
    // fmap(identity[A]) == identity[F[A]]
    assert(writer.fmap(identity[Int]) == identity[Writer[String, Int]](writer))
  }
}
