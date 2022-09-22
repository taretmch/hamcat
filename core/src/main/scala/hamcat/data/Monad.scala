package hamcat.data

/** Monad */
trait Monad[M[_]]:

  def flatMap[A, B](f: A => M[B])(ma: M[A]): M[B]

  def flatten[A](mma: M[M[A]]): M[A] = flatMap(identity[M[A]])(mma)

  def pure[A](a: A): M[A]
