package category.syntax

import category.data.Functor

/** Syntax for functor */
trait FunctorSyntax {
  implicit class FunctorOps[F[_], A](v: F[A])(implicit functor: Functor[F]) {
    def fmap[B](f: A => B): F[B] = functor.fmap[A, B](v)(f)
  }
}
