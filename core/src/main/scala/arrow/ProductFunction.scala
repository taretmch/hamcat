package hamcat.arrow

import hamcat.data.identity

/** ProductFunction: Wrapper class of morphism in product category
 *
 * Product Category
 * 1. Objects and Morphisms in product category
 * Let `(A, B)` be objects
 * Let `ProductFunction[A, B, C, D]` be morphism
 *  from object `(A, B)` to object `(C, D)`
 *
 * 2. Composition of morphisms
 * Let `g compose f` and `f andThen g` be compositions
 *   of morphisms `f: ProductFunction[A, B, C, D]` and `g: ProductFunction[C, D, E, H]`
 *
 * 3. Identity morphism
 * Let `productIdentity: ProductFunction[A, B]` be the identity morphism
 */
case class ProductFunction[A, B, C, D](run: (A => C, B => D)) {

  /** Apply method */
  def apply(obj: (A, B)): (C, D) = run match {
    case (f, g) => (f(obj._1), g(obj._2))
  }

  /** Composition of morphism in product category */
  def andThen[E, H](v: ProductFunction[C, D, E, H]): ProductFunction[A, B, E, H] =
    run match {
      case (f, g) => v.run match {
        case (h, k) => ProductFunction((f andThen h, g andThen k))
      }
    }

  /** Composition of morphism in product category */
  def compose[E, H](v: ProductFunction[E, H, A, B]): ProductFunction[E, H, C, D] =
    run match {
      case (f, g) => v.run match {
        case (h, k) => ProductFunction((f compose h, g compose k))
      }
    }
}

/** Companion object */
object ProductFunction {

  def apply[A, B, C, D](f: A => C)(g: B => D): ProductFunction[A, B, C, D] =
    ProductFunction((f, g))

  /** Identity morphism */
  def productIdentity[A, B]: ProductFunction[A, B, A, B] =
    ProductFunction(identity[A], identity[B])
}
