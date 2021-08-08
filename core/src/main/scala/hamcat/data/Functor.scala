package hamcat.data

// Functor
trait Functor[F[_]] {

  def fmap[A, B](f: A => B): F[A] => F[B]
}

object Functor {

  /** Access to implicit instance */
  def apply[F[_]](implicit f: Functor[F]): Functor[F] = f
}
