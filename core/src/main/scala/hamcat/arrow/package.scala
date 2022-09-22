package hamcat

package object arrow:

  /** Alias for FunctionK */
  type ~>[F[_], G[_]] = FunctionK[F, G]
