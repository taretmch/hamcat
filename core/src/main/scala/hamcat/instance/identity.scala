package hamcat.instance

import hamcat.Functor
import hamcat.data.Identity

/** Instances for Identity */
trait IdentityInstances:

  // Functor
  given Functor[Identity] with
    def fmap[A, B](f: A => B): Identity[A] => Identity[B] = _.fmap(f)

object identity extends IdentityInstances
