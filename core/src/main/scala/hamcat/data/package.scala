package hamcat

package object data {

  /** Identity type */
  type Id[A] = A

  /** Identity morphism for scala category */
  def identity[A]: A => A = a => a
}
