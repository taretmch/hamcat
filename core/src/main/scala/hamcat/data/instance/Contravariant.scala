package hamcat.data.instance

import hamcat.data.Contravariant

trait ContravariantInstances:

  /** Reader contravariant functor */
  given [R]: Contravariant[[X] =>> Function1[X, R]] with
    def contramap[A, B](f: B => A): Function1[A, R] => Function1[B, R] =
      _ compose f
