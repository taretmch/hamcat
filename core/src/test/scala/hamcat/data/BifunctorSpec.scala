package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given

class BifunctorSpec extends Spec:

  describe("Either Bifunctor") {
    val bifunctor = summon[Bifunctor[Either]]
    val left: Either[Int, String] = Left(3)
    val right: Either[Int, String] = Right("abcdefg")
    it("射の合成を保存する") {
      assertCompositionInt(bifunctor, right)
      assertCompositionInt(bifunctor, left)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentity(bifunctor, right)
      assertIdentity(bifunctor, left)
    }
  }

  describe("Tuple2 Bifunctor") {
    val bifunctor = summon[Bifunctor[Tuple2]]
    val tuple: (Int, String) = (3, "abcdefg")
    it("射の合成を保存する") {
      assertCompositionInt(bifunctor, tuple)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentity(bifunctor, tuple)
    }
  }

  // bimap(h compose f)(k compose g) == bimap(h)(k) compose bimap(f)(g)
  def assertCompositionInt[F[_, _]](bifunctor: Bifunctor[F], value: F[Int, String]) =
    assert(
      bifunctor.bimap(isEven compose increment)(isEven compose strlen)(value)
        ==
      (bifunctor.bimap(isEven)(isEven) compose bifunctor.bimap(increment)(strlen))(value)
    )

  // fmap(identity[A]) == identity[F[A]]
  def assertIdentity[F[_, _], A, B](bifunctor: Bifunctor[F], value: F[A, B]) =
    assert(
      bifunctor.bimap(identity[A])(identity[B])(value)
        ==
      identity[F[A, B]](value)
    )
