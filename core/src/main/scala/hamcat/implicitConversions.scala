package hamcat

import data.Identity

object implicitConversions {

  /** Conversion between `A` and `Identity[A]` */
  implicit def toIdentity[A](value: A): Identity[A] = Identity(value)
}
