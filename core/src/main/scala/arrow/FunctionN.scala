package hamcat.arrow

/** FunctionN: typeclass of natural transformations */
trait FunctionN[F[_], G[_]] { self =>

  /** Apply method */
  def apply[A](fa: F[A]): G[A]

  /** Composition of natural transformation */
  def andThen[H[_]](v: FunctionN[G, H]): FunctionN[F, H] =
    new FunctionN[F, H] {
      def apply[A](fa: F[A]): H[A] = v(self(fa))
    }

  /** Composition of natural transformation */
  def compose[H[_]](v: FunctionN[H, F]): FunctionN[H, G] =
    v andThen self
}

/** Companion object */
object FunctionN {

  /** Identity */
  def identity[F[_]]: FunctionN[F, F] = new FunctionN[F, F] {
    def apply[A](fa: F[A]): F[A] = fa
  }
}
