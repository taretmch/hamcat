package hamcat.data

import hamcat.util.Spec
import hamcat.data.instance.Implicits.given

class FunctorSpec extends Spec:

  describe("Const Functor") {
    val const: Const[String, Int] = Const("constat string")
    val functor = summon[Functor[Const[String, *]]]

    it("射の合成を保存する") {
      assertCompositionInt(functor, const)
    }

    it("恒等射を恒等射へ写す") {
      assertIdentityInt(functor, const)
    }
  }

  describe("Reader Functor") {
    val functor = summon[Functor[Function1[String, *]]]
    val reader  = strlen
    val param   = "abcdefg"

    it("射の合成を保存する") {
      assert(
        functor.fmap(isEven compose increment)(reader)(param)
          ==
        (functor.fmap(isEven) compose functor.fmap(increment))(reader)(param)
      )
    }

    it("恒等射を恒等射へ写す") {
      assert(
        functor.fmap(identity[Int])(reader)(param)
          ==
        identity[Function1[String, Int]](reader)(param)
      )
    }
  }

  describe("Identity Functor") {
    val functor = summon[Functor[Identity]]
    it("射の合成を保存する") {
      assertCompositionInt(functor, 1)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentityInt(functor, 1)
    }
  }

  describe("List Functor") {
    val functor = summon[Functor[List]]
    val list: List[Int] = List(1, 2, 3, 4, 5)
    it("射の合成を保存する") {
      assertCompositionInt(functor, list)
      assertCompositionInt(functor, Nil)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentityInt(functor, list)
      assertIdentityInt(functor, Nil)
    }
  }

  describe("Option Functor") {
    val functor = summon[Functor[Option]]
    val value = Option(1)
    it("射の合成を保存する") {
      assertCompositionInt(functor, value)
      assertCompositionInt(functor, None)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentityInt(functor, value)
      assertIdentityInt(functor, None)
    }
  }

  describe("Writer Functor") {
    val functor = summon[Functor[Writer[String, *]]]
    val value = Writer("This is a log", 3)
    it("射の合成を保存する") {
      assertCompositionInt(functor, value)
    }
    it("恒等射を恒等射へ写す") {
      assertIdentityInt(functor, value)
    }
  }

  // fmap(g compose f) == fmap(g) compose fmap(f)
  def assertCompositionInt[F[_]](functor: Functor[F], value: F[Int]) =
    assert(
      functor.fmap(isEven compose increment)(value)
        ==
      (functor.fmap(isEven) compose functor.fmap(increment))(value)
    )

  // fmap(identity[A]) == identity[F[A]]
  def assertIdentityInt[F[_], A](functor: Functor[F], value: F[A]) =
    assert(
      functor.fmap(identity[A])(value)
        ==
      identity[F[A]](value)
    )
