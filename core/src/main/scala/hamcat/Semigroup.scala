package hamcat

trait Semigroup[M]:

  def combine(m1: M, m2: M): M
