package hamcat.data.instance

import hamcat.data.{ Functor, Id, Const }

/** Instances of functor */
trait FunctorInstances {

  /** Option functor */
  implicit def optionFunctor: Functor[Option] = new Functor[Option] {
    def fmap[A, B](f: A => B): Option[A] => Option[B] = _.map(f)
  }

  /** List functor */
  implicit def listFunctor: Functor[List] = new Functor[List] {
    def fmap[A, B](f: A => B): List[A] => List[B] = _.map(f)
  }

  /** Reader functor */
  implicit def readerFunctor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
    def fmap[A, B](f: A => B): (R => A) => (R => B) = fa =>
      f compose fa
  }

  /** Identity functor */
  implicit def identityFunctor: Functor[Id] = new Functor[Id] {
    def fmap[A, B](f: A => B): Id[A] => Id[B] = f(_)
  }

  /** Const functor */
  implicit def constFunctor[C]: Functor[Const[C, ?]] = new Functor[Const[C, ?]] {
    def fmap[A, B](f: A => B): Const[C, A] => Const[C, B] = fa =>
      Const(fa.v)
  }
}