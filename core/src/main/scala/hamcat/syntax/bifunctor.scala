package hamcat.syntax

import scala.annotation.targetName

import hamcat.data.Bifunctor

/** Syntax for bifunctor */
trait BifunctorSyntax:
  extension [F[_, _], A, B](v: F[A, B])(using bifunctor: Bifunctor[F])
    @targetName("bifunctor_bimap")
    def bimap[C, D](f: A => C)(g: B => D): F[C, D] =
      bifunctor.bimap(f)(g)(v)
