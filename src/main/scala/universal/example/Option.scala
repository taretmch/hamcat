package category.universal.example

import category.universal.Coproduct2

/** trait of Some or None value */
sealed trait Option[+A] extends Coproduct2[Unit, A]

object Option {
  def apply[A](v: A): Option[A] = if (v == null) None else Some(v)
}

/** Subclass of Option
 *
 * `Some` class has a value of type `A`
 */
case class Some[+A](v: A) extends Option[A]

case object None extends Option[Nothing]
