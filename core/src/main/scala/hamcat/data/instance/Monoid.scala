package hamcat.data.instance

import hamcat.data.{ Monoid, Semigroup }

/** Instances of monoid */
trait MonoidInstances {

  /** Int monoid */
  implicit def monoidForInt(implicit sg: Semigroup[Int]): Monoid[Int] =
    new Monoid[Int] {
      def combine(a: Int, b: Int): Int = sg.combine(a, b)
      def empty: Int = 0
    }

  /** String monoid */
  implicit def monoidForString(implicit sg: Semigroup[String]): Monoid[String] =
    new Monoid[String] {
      def combine(a: String, b: String): String = sg.combine(a, b)
      def empty: String = ""
    }

  /** Option monoid */
  implicit def monoidForOption[A](implicit sgA: Semigroup[A], sgOpt: Semigroup[Option[A]]): Monoid[Option[A]] =
    new Monoid[Option[A]] {
      def combine(a: Option[A], b: Option[A]): Option[A] = sgOpt.combine(a, b)
      def empty: Option[A] = None
    }
}
