package hamcat.data

// Contravariant functor
trait Contravariant[F[_]] {
  def contramap[A, B](f: B => A): F[A] => F[B]
}

object Contravariant {

  /** Access to implicit instance */
  def apply[F[_]](implicit co: Contravariant[F]): Contravariant[F] = co
}
