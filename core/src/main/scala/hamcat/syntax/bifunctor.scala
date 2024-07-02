package hamcat.syntax

import hamcat.Bifunctor

/** Syntax for bifunctor */
trait BifunctorSyntax:
  extension[F[_, _], A, B](v: F[A, B])(using bifunctor: Bifunctor[F])
    def bimap[C, D](f: A => C, g: B => D): F[C, D] =
      bifunctor.bimap(f, g)(v)

object bifunctor extends BifunctorSyntax
