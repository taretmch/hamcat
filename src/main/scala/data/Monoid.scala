package category.data

// Monoid
trait Monoid[M] extends Semigroup[M] {

  /** Unit element of binary operation */
  def empty: M
}
