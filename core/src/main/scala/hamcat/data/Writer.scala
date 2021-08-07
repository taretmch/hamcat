package hamcat.data

import hamcat.implicits._

/** Writer monad */
case class Writer[L, A](run: (L, A)) {

  def flatMap[B](m2: A => Writer[L, B])(implicit sg: Semigroup[L]): Writer[L, B] =
    (identity[Writer[L, A]] >=> m2)(this)

  def fmap[B](f: A => B)(implicit mn: Monoid[L]): Writer[L, B] =
    (identity[Writer[L, A]] >=> (a => Writer.pure[L, B](f(a))))(this)
}

/** Companion Object */
object Writer {

  def apply[L, A](l: L, a: A): Writer[L, A] = Writer((l, a))

  /** Identity */
  def pure[L, A](a: A)(implicit mn: Monoid[L]): Writer[L, A] = Writer((mn.empty, a))

  /** Composition of morphism */
  implicit class WriterOps[L, A, B](m1: A => Writer[L, B])(implicit sg: Semigroup[L]) {
    def >=>[C](m2: B => Writer[L, C]): A => Writer[L, C] = a => {
      val (logB, b) = m1(a).run
      val (logC, c) = m2(b).run
      Writer((logB |+| logC, c))
    }
  }
}
