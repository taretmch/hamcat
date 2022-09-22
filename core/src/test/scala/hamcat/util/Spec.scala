package hamcat.util

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

trait Spec extends AnyFunSpec with Matchers:

  // Functions
  def increment: Int => Int = _ + 1
  def isEven: Int => Boolean = _ % 2 == 0
  def negate: Boolean => Boolean = !_
  def strlen: String => Int = _.length
  val boolToInt:   Boolean => Int =
    case false => 0
    case true => 1
