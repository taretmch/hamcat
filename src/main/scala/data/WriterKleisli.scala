package category.data

/** Kleisli category for writer monad */
object WriterKleisli {

  /** Object type */
  type Writer[A] = (A, String)

  /** Morphism */
  implicit class KleisliOps[A, B](m1: A => Writer[B]) {
    def >=>[C](m2: B => Writer[C]): A => Writer[C] =
      x => {
        val (y, s1) = m1(x)
        val (z, s2) = m2(y)
        (z, s1 + s2)
      }
  }

  /** Identity */
  def pure[A](x: A): Writer[A] = (x, "")
}
