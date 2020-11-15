package category.data

// Semigroup
trait Semigroup[M] {

  /** Binary operation which must be associative */
  def combine(m1: M, m2: M): M
}
