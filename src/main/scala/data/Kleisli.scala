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

  /** Example functions */
  val upCase:  String => Writer[String]       = s => (s.toUpperCase,       "upCase " )
  val toWords: String => Writer[List[String]] = s => (s.split(' ').toList, "toWords ")
  /** Examle of composition */
  val process: String => Writer[List[String]] = upCase >=> toWords
}
