package category.data.instance

import category.data.Semigroup

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
}
