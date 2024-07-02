package hamcat.arrow

/** Natural transformation */
trait FunctionK[F[_], G[_]]:

  def apply[A](fa: F[A]): G[A]

end FunctionK

/** Companion object */
object FunctionK:

  /** Identity */
  def identityK[F[_]]: FunctionK[F, F] = new FunctionK[F, F]:
    def apply[A](fa: F[A]): F[A] = fa

  extension[F[_], G[_]](fg: FunctionK[F, G])

    /** Composition of natural transformation */
    def andThen[H[_]](gh: FunctionK[G, H]): FunctionK[F, H] =
      new FunctionK[F, H]:
        def apply[A](fa: F[A]): H[A] = gh(fg(fa))

    /** Composition of natural transformation */
    def compose[H[_]](hf: FunctionK[H, F]): FunctionK[H, G] =
      new FunctionK[H, G]:
        def apply[A](ha: H[A]): G[A] = fg(hf(ha))

  end extension
