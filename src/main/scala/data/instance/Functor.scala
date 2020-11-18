package category.data.instance

import category.data.Functor
import category.universal.example.{ List, Nil, Cons }

/** Instances of functor */
trait FunctorInstances {

  /** Option functor */
  implicit val OptionFunctor: Functor[Option] = new Functor[Option] {
    def fmap[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
      case None    => None
      case Some(a) => Some(f(a))
    }
  }

  /** List functor */
  implicit val ListFunctor: Functor[List] = new Functor[List] {
    def fmap[A, B](fa: List[A])(f: A => B): List[B] = fa match {
      case Nil        => Nil
      case Cons(h, t) => Cons(f(h), fmap(t)(f))
    }
  }
}
