package hamcat.syntax

import hamcat.data.Profunctor

/** Syntax for profunctor */
trait ProfunctorSyntax:
  extension [F[_, _], A, B](v: F[A, B])(using profunctor: Profunctor[F])
    def bimap[C, D](f: C => A)(g: B => D): F[C, D] =
      profunctor.bimap(f)(g)(v)

    def contramap[C](f: C => A): F[C, B] =
      profunctor.contramap(f)(v)

    def fmap[D](g: B => D): F[A, D] =
      profunctor.fmap(g)(v)
