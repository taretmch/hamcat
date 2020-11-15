package category.data

// Monoid
trait Monoid[M] extends Semigroup[M] {

  /** Binary operation which must be associative */
  def combine(m1: M, m2: M): M

  /** Unit element of binary operation */
  def empty: M
}
