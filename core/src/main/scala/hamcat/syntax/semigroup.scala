package hamcat.syntax

import hamcat.Semigroup

/** Syntax for semigroup */
trait SemigroupSyntax:
  extension [A: Semigroup](lhs: A)(using semigroup: Semigroup[A])

    /** Alias of combine method
     *
     * Usage:
     * ```
     * scala> import hamcat.Implicits._
     *
     * scala> 1 |+| 3
     * val res0: Int = 4
     * ```
     */
    def |+|(rhs: A): A =
      semigroup.combine(lhs, rhs)

    def combine(rhs: A): A =
      semigroup.combine(lhs, rhs)

object semigroup extends SemigroupSyntax
