package category.universal.example

import category.universal.Coproduct2

/** trait of list */
sealed trait List[+A] extends Coproduct2[Unit, A]

object List {
  def apply[A](v: A): List[A] = if (v == null) Nil else Cons(v, Nil)
}

case object Nil extends List[Nothing]
case class Cons[+A](h: A, t: List[A]) extends List[A]
