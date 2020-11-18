package category.syntax

import category.data.Semigroup

/** Syntax for semigroup */
trait SemigroupSyntax {
  implicit class SemigroupOps[A: Semigroup](lhs: A)(implicit semigroup: Semigroup[A]) {

    /** Alias of combine method
     *
     * Usage:
     * ```
     * scala> import category.Implicits._
     *
     * scala> 1 |+| 3
     * val res0: Int = 4
     * ```
     */
    def |+|(rhs: A): A = semigroup.combine(lhs, rhs)
    def combine(rhs: A): A = semigroup.combine(lhs, rhs)
  }
}
