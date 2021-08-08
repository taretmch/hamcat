package hamcat.data.instance

import hamcat.data.{ Functor, Identity, Const }

/** Instances of functor */
trait FunctorInstances {

  /** Option functor */
  implicit def functorForOption: Functor[Option] =
    new Functor[Option] {
      def fmap[A, B](f: A => B): Option[A] => Option[B] = _.map(f)
    }

  /** List functor */
  implicit def functorForList: Functor[List] =
    new Functor[List] {
      def fmap[A, B](f: A => B): List[A] => List[B] = _.map(f)
    }

  /** Reader functor */
  implicit def functorForFunction1[R]: Functor[Function1[R, *]] =
    new Functor[Function1[R, *]] {
      def fmap[A, B](f: A => B): (R => A) => (R => B) = fa =>
        f compose fa
    }

  /** Identity functor */
  implicit def functorForIdentity: Functor[Identity] =
    new Functor[Identity] {
      def fmap[A, B](f: A => B): Identity[A] => Identity[B] = _.map(f)
    }

  /** Const functor */
  implicit def functorForConst[C]: Functor[Const[C, *]] =
    new Functor[Const[C, *]] {
      def fmap[A, B](f: A => B): Const[C, A] => Const[C, B] = fa =>
        Const(fa.v)
    }
}
