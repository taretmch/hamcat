package hamcat.data.instance

import hamcat.data.Semigroup

/** Instances of semigroup */
trait SemigroupInstances {

  /** Int with sum instance */
  implicit val IntSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def combine(a: Int, b: Int): Int = a + b
  }

  /** String with sum instance */
  implicit val StringSemigroup: Semigroup[String] = new Semigroup[String] {
    def combine(a: String, b: String): String = a + b
  }

  implicit def OptionSemigroup[A: Semigroup](implicit semigroup: Semigroup[A]): Semigroup[Option[A]] =
    new Semigroup[Option[A]] {
      def combine(a: Option[A], b: Option[A]): Option[A] =
        (a, b) match {
          case (None,     None)     => None
          case (None,     Some(a2)) => Some(a2)
          case (Some(a1), None)     => Some(a1)
          case (Some(a1), Some(a2)) => Some(semigroup.combine(a1, a2))
        }
    }
}
