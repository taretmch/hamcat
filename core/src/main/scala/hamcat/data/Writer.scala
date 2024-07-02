package hamcat.data

import hamcat.syntax.SemigroupSyntax

import hamcat.{ Semigroup, Monoid }

/** Data type: Writer */
case class Writer[L, A](run: (L, A)):

  val log: L = run._1

  val value: A = run._2

  def flatMap[B](m2: A => Writer[L, B])(using Semigroup[L]): Writer[L, B] =
    ((identity[Writer[L, A]]) >=> m2)(this)

  def fmap[B](f: A => B)(using Monoid[L]): Writer[L, B] =
    (identity[Writer[L, A]] >=> (a => Writer.pure[L, B](f(a))))(this)

object Writer extends SemigroupSyntax:

  def apply[L, A](l: L, a: A): Writer[L, A] = Writer((l, a))

  /** Identity */
  def pure[L, A](a: A)(using mn: Monoid[L]): Writer[L, A] = Writer((mn.empty, a))

  /** Composition of morphism */
  extension [L, A, B](m1: A => Writer[L, B])(using Semigroup[L])
    def >=>[C](m2: B => Writer[L, C]): A => Writer[L, C] = a =>
      val (logB, b) = m1(a).run
      val (logC, c) = m2(b).run
      Writer((logB |+| logC, c))
