package hamcat.data

// Monoid
trait Monoid[M] extends Semigroup[M] {

  /** Unit element of binary operation */
  def empty: M
}

object Monoid {

  /** Access to implicit instance */
  def apply[A](implicit m: Monoid[A]): Monoid[A] = m
}
