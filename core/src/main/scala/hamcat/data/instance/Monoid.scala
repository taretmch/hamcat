package hamcat.data.instance

import hamcat.data.{ Monoid, Semigroup }

/** Instances of monoid */
trait MonoidInstances {

  /** Int monoid */
  implicit def IntMonoid(implicit sg: Semigroup[Int]): Monoid[Int] = new Monoid[Int] {
    def combine(a: Int, b: Int): Int = sg.combine(a, b)
    def empty: Int = 0
  }

  /** String monoid */
  implicit def StringMonoid(implicit sg: Semigroup[String]): Monoid[String] = new Monoid[String] {
    def combine(a: String, b: String): String = sg.combine(a, b)
    def empty: String = ""
  }

  /** Option monoid */
  implicit def OptionMonoid[A](implicit sgA: Semigroup[A], sgOpt: Semigroup[Option[A]]): Monoid[Option[A]] =
    new Monoid[Option[A]] {
      def combine(a: Option[A], b: Option[A]): Option[A] = sgOpt.combine(a, b)
      def empty: Option[A] = None
    }

  /** List monoid */
  implicit def ListMonoid[A](implicit sg: Semigroup[List[A]]): Monoid[List[A]] =
    new Monoid[List[A]] {
      def combine(a: List[A], b: List[A]): List[A] = sg.combine(a, b)
      def empty: List[A] = Nil
    }
}
