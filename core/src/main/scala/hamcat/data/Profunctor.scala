package hamcat.data

// Profunctor
trait Profunctor[F[_, _]] {

  def bimap[A, B, C, D](f: C => A)(g: B => D): F[A, B] => F[C, D]

  /** Contravariant mapping */
  def contramap[A, B, C](f: C => A): F[A, B] => F[C, B] =
    bimap(f)(identity[B])

  /** Covariant mapping */
  def fmap[A, B, D](g: B => D): F[A, B] => F[A, D] =
    bimap(identity[A])(g)
}
