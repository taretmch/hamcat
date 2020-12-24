package hamcat.arrow

/** FunctionK: typeclass for mapping between first-order-kinded types
 *
 * Natural transformation which is a mapping between two Functors is a subset of FunctionK.
 * Natural transformation must satisfy following naturality condition:
 *
 * {{{
 * scala> val headOption = new FunctionK[List, Option] { def apply[A](fa: List[A]): Option[A] = fa.headOption }
 * scala> val list = List(1, 2, 3, 4, 5)
 * scala> def isEven(num: Int): Boolean = num % 2 == 0
 * scala> (OptionFunctor.fmap(isEven) compose headOption[Int] _)(list) == (headOption[Boolean] _ compose ListFunctor.fmap(isEven))(list)
 * val res1: Boolean = true
 * }}}
 */
trait FunctionK[F[_], G[_]] { self =>

  /** Apply method */
  def apply[A](fa: F[A]): G[A]

  /** Composition of natural transformation */
  def andThen[H[_]](v: FunctionK[G, H]): FunctionK[F, H] =
    new FunctionK[F, H] {
      def apply[A](fa: F[A]): H[A] = v(self(fa))
    }

  /** Composition of natural transformation */
  def compose[H[_]](v: FunctionK[H, F]): FunctionK[H, G] =
    v andThen self
}

/** Companion object */
object FunctionK {

  /** Identity */
  def identityK[F[_]]: FunctionK[F, F] = new FunctionK[F, F] {
    def apply[A](fa: F[A]): F[A] = fa
  }
}
