package category.universal

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

  /** Build method */
  def apply[A, B](a: A, b: B): ProductCategory[A, B] =
    ProductCategory((a, b))

  /** Morphism in product category */
  def biMorp[A, B, C, D](f: A => C)(g: B => D): BiObj[A, B] => BiObj[C, D] = {
    case (a, b) => (f(a), g(b))
  }

  /** Identity morphism */
  def pure[A, B](obj: BiObj[A, B]): BiObj[A, B] =
    biMorp(identity[A])(identity[B])(obj)

  /** Composition of morphism */
  implicit class Ops[A, B, C, D](lhs: BiObj[A, B] => BiObj[C, D]) {
    def andThen[E, H](rhs: BiObj[C, D] => BiObj[E, H]): BiObj[A, B] => BiObj[E, H] = {
      case (a, b) => rhs(lhs((a, b)))
    }
  }
}
