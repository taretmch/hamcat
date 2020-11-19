package category.data

import category.Implicits._

/** Writer monad */
case class Writer[L, A](run: (L, A)) {

  def flatMap[B](m2: A => Writer[L, B])(implicit logSemigroup: Semigroup[L]): Writer[L, B] = {
    val (log1, a) = run
    val (log2, b) = m2(a).run
    Writer((log1 |+| log2, b))
  }
}

/** Companion Object */
object Writer {

  /** Identity */
  def pure[L, A](a: A)(implicit logMonoid: Monoid[L]): Writer[L, A] = Writer((logMonoid.empty, a))

  /** Morphism */
  implicit class WriterOps[L, A, B](m1: A => Writer[L, B])(implicit logGroup: Semigroup[L]) {
    def >=>[C](m2: B => Writer[L, C]): A => Writer[L, C] =
      a => m1(a).flatMap(m2(_))
  }
}
