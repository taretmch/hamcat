package hamcat.data.instance

import hamcat.data.Contravariant

trait ContravariantInstances {

  /** Reader contravariant functor */
  implicit def Function1Contravariant[R]: Contravariant[Function1[?, R]] = new Contravariant[Function1[?, R]] {
    def contramap[A, B](f: B => A): Function1[A, R] => Function1[B, R] = fa =>
      fa compose f
  }
}
