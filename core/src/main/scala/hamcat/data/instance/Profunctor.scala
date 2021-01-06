package hamcat.data.instance

import hamcat.data.Profunctor

/** Instances of profunctor */
trait ProfunctorInstances {

  /** Reader profunctor */
  implicit val Function1Profunctor = new Profunctor[Function1] {
    def bimap[A, B, C, D](f: C => A)(g: B => D): (A => B) => (C => D) = fab =>
      g compose fab compose f
  }
}
