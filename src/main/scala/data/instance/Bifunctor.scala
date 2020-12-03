package category.data.instance

import category.data.Bifunctor

/** Instances of bifunctor */
trait BifunctorInstances {

  /** Product bifunctor */
  implicit val Tuple2Bifunctor = new Bifunctor[Tuple2] {
    def bimap[A, B, C, D](f: A => C)(g: B => D): ((A, B)) => ((C, D)) = {
      case (a, b) => (f(a), g(b))
    }
  }

  /** Coproduct bifunctor */
  implicit val EitherBifunctor = new Bifunctor[Either] {
    def bimap[A, B, C, D](f: A => C)(g: B => D): Either[A, B] => Either[C, D] = {
      case Left(a)  => Left(f(a))
      case Right(b) => Right(g(b))
    }
  }
}
