package hamcat.example.tut.chapter10

import hamcat.Implicits._
import hamcat.arrow.~>
import hamcat.data.identity

/** モナドの定義
 *
 * 以下3つがモナドの公理と呼ばれる。
 * 1. `mu[A] compose T[eta[A]] == identity[T[A]]`
 * 2. `mu[A] compose eta[T[A]] == identity[T[A]]`
 * 3. `mu[A] compose mu[T[A]] == mu[A] compose T[mu[A]]`
 */
object Monad {

  /** Option モナド
   *
   * 自己関手 T   = Option
   * 自然変換 eta = Option.apply
   * 自然変換 mu  = Option#flatten
   */
  object OptionMonad {
    type Id[A] = A
    type OptionOption[A] = Option[Option[A]]

    // 自然変換 eta: Id ~> T
    object eta extends (Id ~> Option) {
      def apply[A](fa: Id[A]): Option[A] = Option(fa)
    }
    // 自然変換 mu: TT -> T
    object mu  extends (OptionOption ~> Option) {
      def apply[A](fa: Option[Option[A]]): Option[A] = fa.flatten
    }

    // モナドの公理1つ目
    val laws1 = (
      (mu[Int] _ compose OptionFunctor.fmap(eta[Int]))(Option(3))
        ==
      identity[Option[Int]](Option(3))
    )

    // モナドの公理2つ目
    val laws2 = (
      (mu[Int] _ compose eta[Option[Int]])(Option(3))
       ==
      identity[Option[Int]](Option(3))
    )

    // モナドの公理3つ目
    val laws3 = (
      (mu[Int] _ compose mu[Option[Int]])(Option(Option(Option(3))))
        ==
      (mu[Int] _ compose OptionFunctor.fmap(mu[Int]))(Option(Option(Option(3))))
    )
  }

  // モナドを定義する
  object MonadDefinition {
    // flatten & pure
    trait Monad1[T[_]] {
      def flatten[A](tta: T[T[A]]): T[A]
      def pure[A](a: A): T[A]
    }

    // flatMap & pure
    trait Monad2[T[_]] {
      def flatMap[A, B](f: A => T[B])(ta: T[A]): T[B]
      def pure[A](a: A): T[A]
    }

    // >=> & pure
    trait Monad3[T[_]] {
      def >=>[A, B, C](m1: A => T[B])(m2: B => T[C]): A => T[C]
      def pure[A](a: A): T[A]
    }
  }
}
