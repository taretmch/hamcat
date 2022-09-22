package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given

class ContravariantSpec extends Spec:

  describe("Reader Contravariant Functor") {
    val functor = summon[Contravariant[Function1[*, Boolean]]]
    val func: Function1[Int, Boolean] = _ % 2 == 1
    val param = 3

    it("射の合成を保存する") {
      assert(
        functor.contramap(boolToInt compose isEven)(func)(param)
          ==
        (functor.contramap(isEven) compose functor.contramap(boolToInt))(func)(param)
      )
    }

    it("恒等射を恒等射へ写す") {
      assert(
        functor.contramap(identity[Int])(func)(param)
          ==
        identity[Function1[Int, Boolean]](func)(param)
      )
    }
  }

  // bimap(g compose f) == bimap(g) compose bimap(f)
  //def assertCompositionInt[F[_]](functor: Contravariant[F], value: F[Int]) =
  //  assert(
  //    functor.contramap(boolToInt compose isEven)(value)
  //      ==
  //    (functor.contramap(isEven) compose functor.contramap(boolToInt))(value)
  //  )

  //def assertIdentity[F[_], A](functor: Contravariant[F], value: F[A]) =
  //  assert(
  //    functor.contramap(identity[A])(value)
  //      ==
  //    identity[F[A]](value)
  //  )
