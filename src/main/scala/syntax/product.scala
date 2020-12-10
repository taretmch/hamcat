package category.syntax

import category.universal.ProductCategory.BiObj

/** Syntax for ProductCategory */
trait ProductCategorySyntax {
  implicit class BiMorpOps[A, B, C, D](lhs: (A => C, B => D)) {
    def apply(obj: BiObj[A, B]): BiObj[C, D] = lhs match {
      case (f, g) => obj match {
        case (a, b) => (f(a), g(b))
      }
    }

    /** Composition of morphism in product category */
    def andThen[E, H](rhs: (C => E, D => H)): (A => E, B => H) =
      lhs match {
        case (f, g) => rhs match {
          case (h, k) => (h compose f, k compose g)
        }
      }

    /** Composition of morphism in product category */
    def compose[E, H](rhs: (E => A, H => B)): (E => C, H => D) =
      lhs match {
        case (f, g) => rhs match {
          case (h, k) => (f compose h, g compose k)
        }
      }
  }
}
