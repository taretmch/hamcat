package hamcat

trait Monad[M[_]]:

  def flatMap[A, B](f: A => M[B])(ma: M[A]): M[B]

  def pure[A](a: A): M[A]

  def flatten[A](mma: M[M[A]]): M[A] = flatMap(identity[M[A]])(mma)

  def map[A, B](f: A => B)(ma: M[A]): M[B] = flatMap[A, B](a => pure(f(a)))(ma)
