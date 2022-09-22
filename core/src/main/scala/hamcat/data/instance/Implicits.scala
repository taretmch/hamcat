package hamcat.data.instance

trait InstanceImplicits
  extends MonoidInstances
  with    SemigroupInstances
  with    FunctorInstances
  with    BifunctorInstances
  with    ContravariantInstances
  with    ProfunctorInstances

object Implicits extends InstanceImplicits
