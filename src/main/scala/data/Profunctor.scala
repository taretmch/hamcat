package category.data

// Profunctor
trait Profunctor[F[_, _]] {

  def bimap[A, B, C, D](f: C => A)(g: B => D): F[A, B] => F[C, D]
}
