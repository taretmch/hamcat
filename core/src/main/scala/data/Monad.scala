package hamcat.data

/** Monda */
trait Monad[M[_]] {

  def flatMap[A, B](f: A => M[B])(ma: M[A]): M[B]

  def flatten[A](mma: M[M[A]]): M[A] = flatMap(identity[M[A]])(mma)

  def >=>[A, B, C](m1: A => M[B], m2: B => M[C]): A => M[C] = a => flatMap(m2)(m1(a))

  def pure[A](a: A): M[A]
}
