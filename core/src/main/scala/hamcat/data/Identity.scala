package hamcat.data

/** Data type: Identity */
case class Identity[A](value: A):

  def fmap[B](f: A => B): Identity[B] =
    Identity(f(value))
