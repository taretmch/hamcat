package hamcat

trait Profunctor[F[_, _]]:

  def dimap[A, B, C, D](f: C => A)(g: B => D): F[A, B] => F[C, D]

  def contramap[A, B, C](f: C => A): F[A, B] => F[C, B] =
    dimap(f)(identity[B])

  def fmap[A, B, D](g: B => D): F[A, B] => F[A, D] =
    dimap(identity[A])(g)

  def map[A, B, D](g: B => D): F[A, B] => F[A, D] =
    fmap(g)
