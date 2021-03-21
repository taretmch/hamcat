package hamcat.arrow

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import hamcat.arrow.ProductFunction.productIdentity

class ProductFunctionSpec extends AnyFlatSpec with Matchers {
  val obj:       (String, Int)      = ("abcdefg", 44)
  def strToInt:  String  => Int     = _.length
  def increment: Int     => Int     = _ + 1
  def isEven:    Int     => Boolean = _ % 2 == 0
  def negate:    Boolean => Boolean = b => !b

  val func1: ProductFunction[String, Int, Int, Int] =
    ProductFunction(strToInt)(increment)
  val func2: ProductFunction[Int, Int, Boolean, Int] =
    ProductFunction(isEven)(increment)
  val func3: ProductFunction[Boolean, Int, Boolean, Boolean] =
    ProductFunction(negate)(isEven)

  "ProductFunction" should "関数適用できる" in {
    val res1 = func1(obj)
    val res2 = (strToInt(obj._1), increment(obj._2))
    assert(res1._1 == res2._1)
    assert(res1._2 == res2._2)
  }

  "ProductFunction" should "射の合成が結合律を満たす" in {
    val res1 = (func3 compose func2 compose func1)  (obj)
    val res2 = ((func3 compose func2) compose func1)(obj)
    val res3 = (func3 compose (func2 compose func1))(obj)
    assert(res1 == res2 && res2 == res3)

    val res4 = (func1 andThen func2 andThen func3)  (obj)
    val res5 = ((func1 andThen func2) andThen func3)(obj)
    val res6 = (func1 andThen (func2 andThen func3))(obj)
    assert(res4 == res5 && res5 == res6)
  }

  it should "恒等射が存在する" in {
    // contramacontramap(identity[A]) == identity[F[A]]
    assert((productIdentity[Int, Int] compose func1)   (obj) == func1(obj))
    assert((func1 compose productIdentity[String, Int])(obj) == func1(obj))
  }
}
