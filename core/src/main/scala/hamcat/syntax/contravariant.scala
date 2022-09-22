package hamcat.syntax

import hamcat.data.Contravariant

/** Syntax for contravariant functor */
trait ContravariantSyntax:
  extension [F[_], A](v: F[A])(using contravariant: Contravariant[F])
    def contramap[B](f: B => A): F[B] =
      contravariant.contramap(f)(v)
