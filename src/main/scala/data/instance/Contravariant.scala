package category.data.instance

import category.data.Contravariant

trait ContravariantInstances {

  /** Reader contravariant functor */
  implicit def Function1Contravariant[R]: Contravariant[Function1[?, R]] = new Contravariant[Function1[?, R]] {
    def contramap[A, B](f: B => A)(fa: A => R): (B => R) =
      fa compose f
  }
}
