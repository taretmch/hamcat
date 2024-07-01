package hamcat.instance

import hamcat.Bifunctor

/** Instances for Tuple */
trait TupleInstances:

  // Bifunctor
  given Bifunctor[Tuple2] with
    def bimap[A, B, C, D](f: A => C)(g: B => D): ((A, B)) => ((C, D)) =
      case (a, b) => (f(a), g(b))

object tuple extends TupleInstances
