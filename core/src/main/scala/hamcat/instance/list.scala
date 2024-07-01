package hamcat.instance

import hamcat.*

/** Instances for List */
trait ListInstances:

  // Functor
  given Functor[List] with
    def fmap[A, B](f: A => B): List[A] => List[B] = _.map(f)

object list extends ListInstances
