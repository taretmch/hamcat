package hamcat.data

/** Data type: Reader */
case class Reader[-R, +A](run: R => A):

  def fmap[B](f: A => B): Reader[R, B] =
    Reader(run andThen f)

  def contramap[S](f: S => R): Reader[S, A] =
    Reader(run compose f)
