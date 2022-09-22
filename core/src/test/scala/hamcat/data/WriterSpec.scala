package hamcat.data

import hamcat.util.Spec
import hamcat.data.Writer
import hamcat.data.Writer.>=>
import hamcat.data.instance.Implicits.given

class WriterSpec extends Spec:

  // increment の Writer 関数
  def incrementWriter(n: Int): Writer[String, Int] =
    Writer("increment ", increment(n))

  // isEven の Writer 関数
  def isEvenWriter(n: Int): Writer[String, Boolean] =
    Writer("isEven ", isEven(n))

  // negate の Writer 関数
  def negateWriter(b: Boolean): Writer[String, Boolean] =
    Writer("negate ", negate(b))

  describe("Writer 圏") {
    it("結合律を満たす") {
      assert(
        ((incrementWriter _ >=> isEvenWriter) >=> negateWriter)(3)
        == (incrementWriter _ >=> (isEvenWriter _ >=> negateWriter))(3)
      )
    }

    it("単位律を満たす") {
      assert((Writer.pure[String, Int] _ >=> isEvenWriter)(3) == isEvenWriter(3))
      assert((isEvenWriter _ >=> Writer.pure[String, Boolean])(3) == isEvenWriter(3))
    } 
  }
