package category.example

import cats.data._
import cats.implicits._

object WriterTExample {

  /** Create Writer instance */
  val w1: Writer[String, Int] = Writer("w1 ", 1)
  /** Get tuple of writer */
  def getTupleFromWriter[L, V](w: Writer[L, V]): (L, V) = w.run

  /** Morphism */
  def negate(b: Boolean): Writer[String, Boolean] = Writer("negate ", !b)
  def isEven(n: Int):     Writer[String, Boolean] = Writer("isEven ", n % 2 == 0)

  /** Composition of morphism */
  def isOdd(n: Int): Writer[String, Boolean] = isEven(n).flatMap(negate)
}
