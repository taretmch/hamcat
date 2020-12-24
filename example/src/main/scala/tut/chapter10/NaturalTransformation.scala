package hamcat.example.tut.chapter10

import hamcat.Implicits._
import hamcat.data.Const

/** 自然変換の具体例
 *
 * 自然変換の具体例を通して、自然変換の性質を学ぶ
 */
object NaturalTransformation {
  def isEven: Int => Boolean = _ % 2 == 0

  object ListToOption {
    def headOption[A]: List[A] => Option[A] = {
      case head :: tail => Some(head)
      case Nil          => None
    }
    def listToNone[A]: List[A] => Option[A] = _ => None

    val list = List(1, 2, 3, 4, 5)

    val v1 = (OptionFunctor.fmap(isEven) compose headOption[Int])(list)
    val v2 = (headOption[Boolean] compose ListFunctor.fmap(isEven))(list)

    val v3 = (OptionFunctor.fmap(isEven) compose listToNone[Int])(list)
    val v4 = (listToNone[Boolean] compose ListFunctor.fmap(isEven))(list)
  }

  object OptionToList {
    def toList[A]: Option[A] => List[A] = {
      case Some(a) => List(a)
      case None    => Nil
    }
    def optionToNil[A]: Option[A] => List[A] = _ => Nil

    val option = Option(3)

    val v1 = (ListFunctor.fmap(isEven) compose toList[Int])(option)
    val v2 = (toList[Boolean] compose OptionFunctor.fmap(isEven))(option)
  }

  object ListListToList {
    def flattenListList[A]: List[List[A]] => List[A] = _.flatten
    def fmapLL[A, B]: (A => B) => List[List[A]] => List[List[B]] = f => listA =>
      listA.fmap(_.fmap(f))

    val listlist = List(List(1, 2, 3), List(4, 5), List(6))

    val v1 = (ListFunctor.fmap(isEven) compose flattenListList[Int])(listlist)
    val v2 = (flattenListList[Boolean] compose fmapLL(isEven))(listlist)
  }

  object ListOptionToList {
    def flattenListOption[A]: List[Option[A]] => List[A] = _.flatten
    def fmapLO[A, B]: (A => B) => List[Option[A]] => List[Option[B]] = f => listA =>
      listA.fmap(_.fmap(f))

    val listOption = List(Some(1), Some(2), None, Some(3))
    val v1 = (ListFunctor.fmap(isEven) compose flattenListOption[Int])(listOption)
    val v2 = (flattenListOption[Boolean] compose fmapLO(isEven))(listOption)
  }

  object ListToConst {
    def length[A]: List[A] => Const[Int, A] = list => Const(list.length)

    val list = List(1, 2, 3, 4, 5)

    val v1 = (ConstFunctor.fmap(isEven) compose length[Int])(list)
    val v2 = (length[Boolean] compose ListFunctor.fmap(isEven))(list)
  }

  object ReaderToOption {
    def dumb[A]: Function1[Unit, A] => Option[A] = _ => None
    def obvious[A]: Function1[Unit, A] => Option[A] = f => Some(f())

    val func: Unit => Int = _ => 3

    val v1 = (OptionFunctor.fmap(isEven) compose dumb[Int])(func)
    val v2 = (dumb[Boolean] compose Function1Functor.fmap(isEven))(func)

    val v3 = (OptionFunctor.fmap(isEven) compose obvious[Int])(func)
    val v4 = (obvious[Boolean] compose Function1Functor.fmap(isEven))(func)
  }
}
