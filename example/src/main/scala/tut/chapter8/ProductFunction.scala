package category.example

import category.arrow.ProductFunction

/** Examples of product category */
object ProductCategory {

  def increment: Int => Int = _ + 1
  def doubleL: Long => Long = _ * 2

  def isEven: Int => Boolean = _ % 2 == 0
  def isOddL: Long => Boolean = _ % 2 == 1

  /** Scala の直積圏の例 */
  object Expl1 {
    /** Object declaration */
    val obj = (3, 4L)

    /** Morphism declaration */
    val func1 = ProductFunction(increment, doubleL)
    val func2 = ProductFunction(isEven, isOddL)

    /** Apply morphism to object */
    val func1Apply: (Int, Long)        = func1(obj)
    val func2Apply: (Boolean, Boolean) = func2(obj)

    /** Compose morphism */
    val func2ComposeFunc1 = func2 compose func1
    val func1AndThenFunc2 = func1 andThen func2

    /** Apply composition of morphism */
    val result1 = func2ComposeFunc1(obj)
    val result2 = func1AndThenFunc2(obj)
  }
}
