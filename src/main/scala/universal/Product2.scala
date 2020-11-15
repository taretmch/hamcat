package category.universal

/** trait of product */
abstract case class Product2[+A, +B](v: (A, B)) {

  def projectionA: A = v._1
  def projectionB: B = v._2
}
