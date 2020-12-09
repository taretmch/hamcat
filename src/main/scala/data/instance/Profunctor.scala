package category.data.instance

import category.data.Profunctor

/** Instances of profunctor */
trait ProfunctorInstances {

  /** Reader profunctor */
  implicit val Function1Profunctor = new Profunctor[Function1] {
    def bimap[A, B, C, D](f: C => A)(g: B => D)(fab: A => B): (C => D) =
      g compose fab compose f
  }
}
