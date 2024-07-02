package hamcat.data

/** Data type: Const */
case class Const[C, +A](value: C):

  def fmap[B](f: A => B): Const[C, B] =
    Const[C, B](value)
