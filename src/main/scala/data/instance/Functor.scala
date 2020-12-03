package category.data.instance

import category.data.{ Functor, Monoid, Writer, Id, Const }

/** Instances of functor */
trait FunctorInstances {

  import category.data.identity

  /** Option functor */
  implicit val OptionFunctor: Functor[Option] = new Functor[Option] {
    def fmap[A, B](f: A => B)(fa: Option[A]): Option[B] = fa match {
      case None    => None
      case Some(a) => Some(f(a))
    }
  }

  /** List functor */
  implicit val ListFunctor: Functor[List] = new Functor[List] {
    def fmap[A, B](f: A => B)(fa: List[A]): List[B] =
      fa.map(f)
  }

  /** Writer functor */
  implicit def WriterFunctor[L](implicit mn: Monoid[L]): Functor[Writer[L, ?]] = new Functor[Writer[L, ?]] {
    def fmap[A, B](f: A => B)(fa: Writer[L, A]): Writer[L, B] =
      fa.flatMap((Writer.pure[L, B] _) compose f)
  }

  /** Reader functor */
  implicit def Function1Functor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
    def fmap[A, B](f: A => B)(fa: R => A): (R => B) =
      f compose fa
  }

  /** Identity functor */
  implicit val IdentityFunctor: Functor[Id] = new Functor[Id] {
    def fmap[A, B](f: A => B)(fa: Id[A]): Id[B] = f(fa)
  }

  /** Const functor */
  implicit def constFunctor[C]: Functor[Const[C, ?]] = new Functor[Const[C, ?]] {
    def fmap[A, B](f: A => B)(fa: Const[C, A]): Const[C, B] = Const(fa.v)
  }
}
