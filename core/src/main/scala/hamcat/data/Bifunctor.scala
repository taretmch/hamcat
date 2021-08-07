package hamcat.data

// Bifunctor
trait Bifunctor[F[_, _]] {

  /** Morphism mappings for Bifunctor */
  def bimap[A, B, C, D](f: A => C)(g: B => D): F[A, B] => F[C, D]

  def first[A, B, C](f: A => C): F[A, B] => F[C, B] = bimap(f)(identity[B])
  def second[A, B, D](g: B => D): F[A, B] => F[A, D] = bimap(identity[A])(g)
}

object Bifunctor {

  /** Access to implicit instance */
  def apply[F[_, _]](implicit bf: Bifunctor[F]): Bifunctor[F] = bf
}
