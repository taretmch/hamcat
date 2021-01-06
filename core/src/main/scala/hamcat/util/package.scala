package hamcat

package object util {
  implicit class EqSyntax[A](private val lhs: A) extends AnyVal {
    def ===(rhs: A): Eq2[A] = Eq(lhs, rhs)
  }
}
