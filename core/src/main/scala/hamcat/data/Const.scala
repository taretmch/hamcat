package hamcat.data

/** Const data type */
case class Const[C, +A](v: C):
  def fmap[B](f: A => B): Const[C, B] =
    Const[C, B](v)
