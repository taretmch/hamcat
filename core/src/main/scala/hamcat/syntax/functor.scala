package hamcat.syntax

import hamcat.data.Functor

/** Syntax for functor */
trait FunctorSyntax:
  extension [F[_], A](v: F[A])(using functor: Functor[F])
    def fmap[B](f: A => B): F[B] =
      functor.fmap(f)(v)
