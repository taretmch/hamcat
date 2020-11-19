package category.data

// Functor
trait Functor[F[_]] {

  def fmap[A, B](fa: F[A])(f: A => B): F[B]
}
