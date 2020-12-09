package category.universal.example

import category.Implicits._

import category.data.{ Semigroup, Writer }
import category.universal.ProductCategory
import category.universal.ProductCategory._

/** Examples of product category */
object ProductCategoryExample {

  def increment: Int => Int = _ + 1
  def doubleL: Long => Writer[String, Long] = longNum =>
    Writer("double ", longNum * 2)

  def isEven: Int => Boolean = _ % 2 == 0
  def isEvenL: Long => Writer[String, Boolean] = longNum =>
    Writer("isEven ", longNum % 2 == 0)

  /** Composition of morphism */
  // TODO: Improve implementation
  implicit class WriterOps[L, A, B, C, D](lhs: BiObj[A, B] => BiObj[C, Writer[L, D]])(implicit sg: Semigroup[L]) {
    def >=>[E, H](rhs: BiObj[C, D] => BiObj[E, Writer[L, H]]): BiObj[A, B] => BiObj[E, Writer[L, H]] = biObj => {
      val (c, writerD) = lhs(biObj)
      val (log1, d)    = writerD.run
      val (e, writerH) = rhs((c, d))
      val (log2, h)    = writerH.run
      (e, Writer(log1 |+| log2, h))
    }
  }

  /** Examples of BiObj */
  object BiObjOps {
    /** Object declaration */
    val biObj: BiObj[Int, Long] = (2, 4L)

    /** Morphism declaration */
    val biMorp1: BiObj[Int, Long] => BiObj[Int, Writer[String, Long]] =
      ProductCategory.biMorp(increment)(doubleL)
    val biMorp2: BiObj[Int, Long] => BiObj[Boolean, Writer[String, Boolean]] =
      ProductCategory.biMorp(isEven)(isEvenL)

    /** Apply morphism to object */
    val biMorpApply: BiObj[Int, Writer[String, Long]] =
      biMorp1(biObj)

    /** Compose morphism */
    val biComp: BiObj[Int, Long] => BiObj[Boolean, Writer[String, Boolean]] =
      biMorp1 >=> biMorp2

    /** Apply composition of morphism */
    val biCompApply: BiObj[Boolean, Writer[String, Boolean]] =
      biComp(biObj)
  }

  /** Examples of ProductCategory */
  object ProductCategoryOps {
    /** Object declartion */
    val biObj: ProductCategory[Int, Writer[String, Long]] =
      ProductCategory(2, Writer("writer product category: odd ", 4L))

    /** Apply morphism */
    val biMorpApplyExpl2: ProductCategory[Int, Writer[String, Long]] =
      biObj.bimap(increment)(_.flatMap(doubleL))

    /** Apply composition of morphism */
    val biCompExpl2: ProductCategory[Boolean, Writer[String, Boolean]] =
      biObj.bimap(isEven compose increment)(_.flatMap(doubleL >=> isEvenL))
  }
}
