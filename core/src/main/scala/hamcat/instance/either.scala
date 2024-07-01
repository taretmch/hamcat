package hamcat.instance

import hamcat.{ Functor, Bifunctor }

/** Instances for Either */
trait EitherInstances:

  // Functor
  given [L]: Functor[[X] =>> Either[L, X]] with
    def fmap[A, B](f: A => B): Either[L, A] => Either[L, B] = _.map(f)

  // Bifunctor
  given Bifunctor[Either] with
    def bimap[A, B, C, D](f: A => C)(g: B => D): Either[A, B] => Either[C, D] =
      case Left(a)  => Left(f(a))
      case Right(b) => Right(g(b))

object either extends EitherInstances
