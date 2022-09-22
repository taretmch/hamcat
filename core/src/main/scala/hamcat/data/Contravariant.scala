package hamcat.data

// Contravariant functor
trait Contravariant[F[_]]:
  def contramap[A, B](f: B => A): F[A] => F[B]
