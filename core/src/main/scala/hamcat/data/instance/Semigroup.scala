package hamcat.data.instance

import hamcat.data.Semigroup

/** Instances of semigroup */
trait SemigroupInstances:

  /** Int with sum instance */
  given Semigroup[Int] with
    def combine(a: Int, b: Int): Int =
      a + b

  /** String with sum instance */
  given Semigroup[String] with
    def combine(a: String, b: String): String =
      a + b

  given [A: Semigroup]: Semigroup[Option[A]] with
    def combine(a: Option[A], b: Option[A]): Option[A] =
      (a, b) match
        case (None,     None)     => None
        case (None,     Some(a2)) => Some(a2)
        case (Some(a1), None)     => Some(a1)
        case (Some(a1), Some(a2)) => Some(summon[Semigroup[A]].combine(a1, a2))
