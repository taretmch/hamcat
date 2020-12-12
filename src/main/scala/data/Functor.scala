package category.data

// Functor
trait Functor[F[_]] {

  def fmap[A, B](f: A => B): F[A] => F[B]
}
