package hamcat.data

/** Identity data type */
case class Identity[A](value: A) {

  def map[B](f: A => B): Identity[B] =
    Identity(f(value))
}
