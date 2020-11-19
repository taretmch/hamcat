package category.universal

/** trait of coproduct */
trait Coproduct2[+A, +B]

/** Subclass of Coproduct2 */
case class Left[A](v: A) extends Coproduct2[A, Nothing]
case class Right[B](v: B) extends Coproduct2[Nothing, B]

object Coproduct2 {

  def injectionA[A, B]: A => Coproduct2[A, B] = Left(_)
  def injectionB[A, B]: B => Coproduct2[A, B] = Right(_)
}
