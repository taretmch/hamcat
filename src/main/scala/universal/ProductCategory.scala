package category.universal

import category.Implicits._
import category.data._

/** Product category */
import ProductCategory._
case class ProductCategory[A, B](run: BiObj[A, B]) {

  def bimap[C, D](f: A => C)(g: B => D): ProductCategory[C, D] =
    ProductCategory(biMorp(f)(g)(run))
}

/** Companion object */
object ProductCategory {

  /** Object type of product category */
  type BiObj[A, B] = (A, B)
  type BiMorp[A, C, B, D] = (A => C, B => D)

  /** Build method */
  def apply[A, B](a: A, b: B): ProductCategory[A, B] =
    ProductCategory((a, b))

  /** Morphism in product category */
  def biMorp[A, B, C, D](f: A => C)(g: B => D): (A => C, B => D) =
    (f, g)

  /** Identity morphism */
  def biIdentity[A, B](obj: BiObj[A, B]): BiObj[A, B] =
    biMorp(identity[A])(identity[B])(obj)
}
