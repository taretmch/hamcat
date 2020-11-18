package category.universal

/** trait of product */
abstract case class Product2[+A, +B](_1: A, _2: B) {

  def projectionA: A = _1
  def projectionB: B = _2
}
