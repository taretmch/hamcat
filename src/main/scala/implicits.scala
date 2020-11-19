package category

import category.data.instance._
import category.syntax._

object Implicits
  extends InstanceImplicits
  with    SyntaxImplicits

trait InstanceImplicits
  extends MonoidInstances
  with    SemigroupInstances
  with    FunctorInstances

trait SyntaxImplicits
  extends SemigroupSyntax
  with    FunctorSyntax
