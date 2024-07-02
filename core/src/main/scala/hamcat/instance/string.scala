package hamcat.instance

import hamcat.*

/** Instances for String */
trait StringInstances:

  // Semigroup
  given Semigroup[String] with
    def combine(a: String, b: String): String =
      a + b

  // Monoid
  given (using sg: Semigroup[String]): Monoid[String] with
    def combine(a: String, b: String): String = sg.combine(a, b)
    def empty: String                         = ""

object string extends StringInstances
