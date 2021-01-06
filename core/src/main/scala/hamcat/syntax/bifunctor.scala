package hamcat.syntax

import hamcat.data.Bifunctor

/** Syntax for bifunctor */
trait BifunctorSyntax {
  implicit class BifunctorOps[F[_, _], A, B](v: F[A, B])(implicit bifunctor: Bifunctor[F]) {
    def bimap[C, D](f: A => C)(g: B => D): F[C, D] = bifunctor.bimap(f)(g)(v)
  }
}
