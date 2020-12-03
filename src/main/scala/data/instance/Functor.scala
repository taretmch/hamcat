package category.data.instance

import category.data.Functor

/** Instances of functor */
trait FunctorInstances {

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

  /** Reader functor */
  implicit def Function1Functor[R]: Functor[Function1[R, ?]] = new Functor[Function1[R, ?]] {
    def fmap[A, B](f: A => B)(fa: R => A): (R => B) =
      f compose fa
  }
}
