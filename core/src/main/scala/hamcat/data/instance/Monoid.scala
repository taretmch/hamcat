package hamcat.data.instance

import hamcat.data.{ Monoid, Semigroup }

/** Instances of monoid */
trait MonoidInstances:

  /** Int monoid */
  given (using sg: Semigroup[Int]): Monoid[Int] with
    def combine(a: Int, b: Int): Int =
      sg.combine(a, b)
    def empty: Int = 0

  /** String monoid */
  given (using sg: Semigroup[String]): Monoid[String] with
    def combine(a: String, b: String): String =
      sg.combine(a, b)
    def empty: String = ""

  /** Option monoid */
  given [A](using sgA: Semigroup[A], sgOpt: Semigroup[Option[A]]): Monoid[Option[A]] with
    def combine(a: Option[A], b: Option[A]): Option[A] = sgOpt.combine(a, b)
    def empty: Option[A] = None
