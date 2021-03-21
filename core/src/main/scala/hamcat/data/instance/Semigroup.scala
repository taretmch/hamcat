package hamcat.data.instance

import hamcat.data.Semigroup

/** Instances of semigroup */
trait SemigroupInstances {

  /** Int with sum instance */
  implicit def intSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def combine(a: Int, b: Int): Int = a + b
  }

  /** String with sum instance */
  implicit def stringSemigroup: Semigroup[String] = new Semigroup[String] {
    def combine(a: String, b: String): String = a + b
  }

/** Option with sum instance */
  implicit def optionSemigroup[A: Semigroup](implicit sg: Semigroup[A]): Semigroup[Option[A]] =
    new Semigroup[Option[A]] {
      def combine(a: Option[A], b: Option[A]): Option[A] =
        (a, b) match {
          case (None,     None)     => None
          case (None,     Some(a2)) => Some(a2)
          case (Some(a1), None)     => Some(a1)
          case (Some(a1), Some(a2)) => Some(sg.combine(a1, a2))
        }
    }

  /** List with concat instance */
  implicit def listSemigroup[A]: Semigroup[List[A]] =
    new Semigroup[List[A]] {
      def combine(a: List[A], b: List[A]): List[A] =
        a ++ b
    }
}