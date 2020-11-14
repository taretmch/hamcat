package category.data.instance

import category.data.Monoid

/** Instances of monoid */
object MonoidInstances {
  implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
    def combine(a: Int, b: Int): Int = a + b
    def empty: Int = 0
  }

  implicit val StringMonoid: Monoid[String] = new Monoid[String] {
    def combine(a: String, b: String): String = a + b
    def empty: String = ""
  }
}
