package hamcat.instance

import hamcat.*

/** Instances for Option */
trait OptionInstances:

  // Semigroup
  given [A: Semigroup]: Semigroup[Option[A]] with
    def combine(a: Option[A], b: Option[A]): Option[A] =
      (a, b) match
        case (None,     None)     => None
        case (None,     Some(a2)) => Some(a2)
        case (Some(a1), None)     => Some(a1)
        case (Some(a1), Some(a2)) => Some(summon[Semigroup[A]].combine(a1, a2))

  // Monoid
  given [A](using sgA: Semigroup[A], sgOpt: Semigroup[Option[A]]): Monoid[Option[A]] with
    def combine(a: Option[A], b: Option[A]): Option[A] = sgOpt.combine(a, b)
    def empty: Option[A] = None

  // Functor
  given Functor[Option] with
    def fmap[A, B](f: A => B): Option[A] => Option[B] = _.map(f)

object option extends OptionInstances
