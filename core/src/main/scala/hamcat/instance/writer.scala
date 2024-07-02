package hamcat.instance

import hamcat.*
import hamcat.data.Writer

/** Instances for Writer */
trait WriterInstances:

  // Functor
  given [L](using Monoid[L]): Functor[[X] =>> Writer[L, X]] with
    def fmap[A, B](f: A => B): Writer[L, A] => Writer[L, B] = _.fmap(f)

object writer extends WriterInstances
