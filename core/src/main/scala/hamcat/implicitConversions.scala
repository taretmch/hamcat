package hamcat

import data.Identity

object implicitConversions:

  /** Conversion between `A` and `Identity[A]` */
  given [A]: Conversion[A, Identity[A]] with
    def apply(value: A) = Identity(value)
