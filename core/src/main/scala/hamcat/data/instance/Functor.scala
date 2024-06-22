package hamcat.data.instance

import hamcat.data.*

/** Instances of functor */
trait FunctorInstances:

  /** Option functor */
  given Functor[Option] with
    def fmap[A, B](f: A => B): Option[A] => Option[B] =
      _.map(f)

  /** List functor */
  given Functor[List] with
    def fmap[A, B](f: A => B): List[A] => List[B] =
      _.map(f)

  /** Reader functor */
  given [R]: Functor[[T] =>> Function1[R, T]] with
    def fmap[A, B](f: A => B): (R => A) => (R => B) =
      f compose _

  /** Identity functor */
  given Functor[Identity] with
    def fmap[A, B](f: A => B): Identity[A] => Identity[B] =
      _.map(f)

  /** Const functor */
  given [C]: Functor[[T] =>> Const[C, T]] with
    def fmap[A, B](f: A => B): Const[C, A] => Const[C, B] =
      _.fmap(f)

  /** Writer functor */
  given [L](using Monoid[L]): Functor[[T] =>> Writer[L, T]] with
    def fmap[A, B](f: A => B): Writer[L, A] => Writer[L, B] =
      _.fmap(f)

  given [F[_], G[_]](using functorF: Functor[F], functorG: Functor[G]): Functor[[X] =>> F[G[X]]] with
    def fmap[A, B](f: A => B): F[G[A]] => F[G[B]] =
      functorF.fmap(ga => functorG.fmap(f)(ga))
