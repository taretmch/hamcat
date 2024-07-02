package hamcat

trait Monoid[M] extends Semigroup[M]:

  def empty: M
