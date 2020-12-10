package category.universal.example

import category.Implicits._

import category.data.Writer
import category.universal.ProductCategory
import category.universal.ProductCategory._

/** Examples of product category */
object ProductCategoryExample {

  def increment: Int => Int = _ + 1
  def doubleL: Long => Long = _ * 2
  def doubleW: Long => Writer[String, Long] = longNum =>
    Writer("double ", longNum * 2)

  def isEven: Int => Boolean = _ % 2 == 0
  def isEvenL: Long => Boolean = _ % 2 == 1
  def isEvenW: Long => Writer[String, Boolean] = longNum =>
    Writer("isEven ", longNum % 2 == 0)

  /** Scala の直積圏の例 */
  object BiObjExpl {
    /** Object declaration */
    val biObj: BiObj[Int, Long] = (2, 4L)

    /** Morphism declaration */
    val biMorp1 = ProductCategory.biMorp(increment)(doubleL)
    val biMorp2 = ProductCategory.biMorp(isEven)(isEvenL)

    /** Apply morphism to object */
    val biMorpApply1: BiObj[Int, Long] = biMorp1(biObj)
    val biMorpApply2: BiObj[Boolean, Boolean] = biMorp2(biObj)

    /** Compose morphism */
    val biComp: (Int => Boolean, Long => Boolean) =
      biMorp1 andThen biMorp2

    /** Apply composition of morphism */
    val biCompApply: (Boolean, Boolean) =
      biComp(biObj)
  }

  /** Scala 圏と Writer 圏の直積圏 */
  object BiObjWriterExpl {
    /** Object declaration */
    val biObj: BiObj[Int, Long] = (2, 4L)

    /** Morphism declaration */
    val biMorp1: (Int => Int, Long => Writer[String, Long]) =
      ProductCategory.biMorp(increment)(doubleW)
    val biMorp2: (Int => Boolean, Long => Writer[String, Boolean]) =
      ProductCategory.biMorp(isEven)(isEvenW)
  }

  /** Examples of ProductCategory */
  object ProductCategoryOps {
    /** Object declartion */
    val biObj: ProductCategory[Int, Writer[String, Long]] =
      ProductCategory(2, Writer("writer product category: odd ", 4L))

    /** Apply morphism */
    val biMorpApplyExpl2: ProductCategory[Int, Writer[String, Long]] =
      biObj.bimap(increment)(_.flatMap(doubleW))

    /** Apply composition of morphism */
    val biCompExpl2: ProductCategory[Boolean, Writer[String, Boolean]] =
      biObj.bimap(isEven compose increment)(_.flatMap(doubleW >=> isEvenW))
  }
}
