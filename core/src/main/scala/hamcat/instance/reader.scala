package hamcat.instance

import hamcat.*
import hamcat.data.Reader

/** Instances for Reader */
trait ReaderInstances:

  // Functor
  given [R]: Functor[[X] =>> Reader[R, X]] with
    def fmap[A, B](f: A => B): Reader[R, A] => Reader[R, B] = reader =>
      Reader(reader.run andThen f)

  // Contravariant
  given [R]: Contravariant[[X] =>> Reader[X, R]] with
    def contramap[A, B](f: B => A): Reader[A, R] => Reader[B, R] = reader =>
      Reader(reader.run compose f)

  // Profunctor
  given Profunctor[Reader] with
    def dimap[A, B, C, D](f: C => A)(g: B => D): Reader[A, B] => Reader[C, D] = reader =>
      Reader(g.compose(reader.run).compose(f))

object reader extends ReaderInstances
