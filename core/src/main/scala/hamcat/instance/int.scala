package hamcat.instance

import hamcat.*

/** Instances for Int */
trait IntInstances:

  // Semigroup
  given Semigroup[Int] with
    def combine(a: Int, b: Int): Int =
      a + b

  // Monoid
  given (using sg: Semigroup[Int]): Monoid[Int] with
    def combine(a: Int, b: Int): Int = sg.combine(a, b)
    def empty: Int                   = 0

object int extends IntInstances
