package category.syntax

import category.data.Contravariant

/** Syntax for contravariant functor */
trait ContravariantSyntax {
  implicit class ContravariantOps[F[_], A](v: F[A])(implicit contravariant: Contravariant[F]) {
    def contramap[B](f: B => A): F[B] = contravariant.contramap(f)(v)
  }
}
