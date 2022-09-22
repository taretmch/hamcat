package hamcat.data

/** Identity data type */
case class Identity[A](value: A):

  def map[B](f: A => B): Identity[B] =
    Identity(f(value))

object Identity:

  /** Conversion between `A` and `Identity[A]` */
  given [A]: Conversion[A, Identity[A]] with
    def apply(value: A) = Identity(value)

  /** Conversion between `F` and `Identity[F[_]]` */
  given ConversionF1[F[_], A]: Conversion[F[A], Identity[F[A]]] with
    def apply(value: F[A]) = Identity(value)

  /** Conversion between `F` and `Identity[F[_]]` */
  given ConversionF2[F[_, _], A, B]: Conversion[F[A, B], Identity[F[A, B]]] with
    def apply(value: F[A, B]) = Identity(value)
