package category.data

/** Kleisli category for writer monad */
object StringWriterKleisli {

  /** Object type */
  type Writer[A] = (String, A)

  /** Morphism */
  implicit class StringWriterKleisliOps[A, B](m1: A => Writer[B]) {
    def >=>[C](m2: B => Writer[C]): A => Writer[C] =
      a => {
        val (log1, b) = m1(a)
        val (log2, c) = m2(b)
        (log1 ++ log2, c)
      }
  }

  /** Identity */
  def pure[A](a: A): Writer[A] = ("", a)
}
