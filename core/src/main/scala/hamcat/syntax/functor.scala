package hamcat.syntax

import hamcat.Functor

/** Syntax for functor */
trait FunctorSyntax:
  extension[F[_], A](v: F[A])(using functor: Functor[F])
    def fmap[B](f: A => B): F[B] =
      functor.fmap(f)(v)

    def map[B](f: A => B): F[B] =
      functor.map(f)(v)

object functor extends FunctorSyntax
