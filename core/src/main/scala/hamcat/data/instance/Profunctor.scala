package hamcat.data.instance

import hamcat.data.Profunctor

/** Instances of profunctor */
trait ProfunctorInstances:

  /** Reader profunctor */
  given Profunctor[Function1] with
    def bimap[A, B, C, D](f: C => A)(g: B => D): (A => B) => (C => D) =
      g compose _ compose f

  given Profunctor[PartialFunction] with
    def bimap[A, B, C, D](f: C => A)(g: B => D): PartialFunction[A, B] => PartialFunction[C, D] =
      pf => {
        case param: C => g(pf(f(param)))
      }
