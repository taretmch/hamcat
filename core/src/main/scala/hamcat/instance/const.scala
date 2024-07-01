package hamcat.instance

import hamcat.Functor
import hamcat.data.Const

/** Instances for Const */
trait ConstInstances:

  // Functor
  given [C]: Functor[[X] =>> Const[C, X]] with
    def fmap[A, B](f: A => B): Const[C, A] => Const[C, B] = _.fmap(f)

object const extends ConstInstances
