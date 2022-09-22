package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given

class ProfunctorSpec extends Spec:

  describe("Reader Profunctor") {
    val profunctor = summon[Profunctor[Function1]]
    val func: Int => String = {
      case n if n < 10  => "less than 10"
      case n if n < 30  => "less than 30"
      case n if n >= 50 => "greater than 50"
      case _            => "hoge"
    }
    val param = 3

    it("射の合成を保存する") {
      assert(
        profunctor.bimap(boolToInt compose isEven)(isEven compose strlen)(func)(param)
          ==
        (profunctor.bimap(isEven)(isEven) compose profunctor.bimap(boolToInt)(strlen))(func)(param)
      )
    }

    it("恒等射を恒等射へ写す") {
      assert(
        profunctor.bimap(identity[Int])(identity[String])(func)(param)
          ==
        identity[Function1[Int, String]](func)(param)
      )
    }
  }

  // bimap(g compose f) == bimap(g) compose bimap(f)
  //def assertCompositionInt[F[_, _]](profunctor: Profunctor[F], value: F[Int, String]) =
  //  assert(
  //    profunctor.bimap(boolToInt compose isEven)(isEven compose strlen)(value)
  //      ==
  //    (profunctor.bimap(isEven)(isEven) compose profunctor.bimap(boolToInt)(strlen))(value)
  //  )

  //def assertIdentity[F[_, _], A, B](profunctor: Profunctor[F], value: F[A, B]) =
  //  assert(
  //    profunctor.bimap(identity[A])(identity[B])(value)
  //      ==
  //    identity[F[A, B]](value)
  //  )
