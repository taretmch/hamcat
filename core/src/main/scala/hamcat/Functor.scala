package hamcat

trait Functor[F[_]]:

  def fmap[A, B](f: A => B): F[A] => F[B]

  def map[A, B](f: A => B): F[A] => F[B] = fmap(f)
