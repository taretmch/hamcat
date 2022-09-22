package hamcat.util

/** `Eq` class represents lhs and rhs are equal
 *
 * This class is used for representing laws
 * such as functor laws, associative law and monad laws etc.
 *
 * Example:
 * {{{
 * scala> def increment: Int => Int = _ + 1
 * scala> def isEven: Int => Boolean = _ % 2 == 0
 * scala> Eq(increment andThen isEven, isEven compose increment)
 * scala> (increment andThen isEven) === (isEven compose increment)
 * scala> def length: String => Int = _.length
 * scala> Eq((isEven compose increment) compose length, isEven compose (increment compose length))
 * scala> ((isEven compose increment) compose length) === (isEven compose (increment compose length))
 * }}}
 */
trait Eq[A]

object Eq:

  def apply[A](lhs: A, rhs: A):             Eq2[A] = Eq2(lhs, rhs)
  def apply[A](_1: A, _2: A, _3: A):        Eq3[A] = Eq3(_1, _2, _3)
  def apply[A](_1: A, _2: A, _3: A, _4: A): Eq4[A] = Eq4(_1, _2, _3, _4)

  case class Eq2[A](lhs: A, rhs: A)             extends Eq[A]
  case class Eq3[A](_1: A, _2: A, _3: A)        extends Eq[A]
  case class Eq4[A](_1: A, _2: A, _3: A, _4: A) extends Eq[A]

  extension [A](lhs: A)
    def ===(rhs: A): Eq.Eq2[A] = Eq(lhs, rhs)
