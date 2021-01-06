package example.arrow

import scala.language.higherKinds
import hamcat.arrow.FunctionK
import hamcat.data._

object FunctionKSample {

  object headOption1 extends FunctionK[List, Option] {
    def apply[A](fa: List[A]): Option[A] = fa.headOption
  }
  val headOption2: FunctionK[List, Option] = new FunctionK[List, Option] {
    def apply[A](fa: List[A]): Option[A] = fa.headOption
  }

  // kind-projector でこんな書き方もできます。
  // わざわざ apply とか new とか extends とか書かなくて OK
  val listToOption      = Lambda[FunctionK[List, Option]](_.headOption)
  type ListOption[A]    = List[Option[A]]
  val flattenListOption = Lambda[FunctionK[ListOption, List]](_.flatten)
}
