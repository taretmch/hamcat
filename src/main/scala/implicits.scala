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
  with    BifunctorInstances
  with    ContravariantInstances
  with    ProfunctorInstances

trait SyntaxImplicits
  extends SemigroupSyntax
  with    FunctorSyntax
  with    BifunctorSyntax
  with    ContravariantSyntax
  with    ProfunctorSyntax
  with    ProductCategorySyntax
