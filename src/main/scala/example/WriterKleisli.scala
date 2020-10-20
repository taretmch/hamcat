package category.example

object WriterKleisliExample {

  import category.data.WriterKleisli._

  /** Example functions */
  val upCase:  String => Writer[String]       = s => (s.toUpperCase,       "upCase " )
  val toWords: String => Writer[List[String]] = s => (s.split(' ').toList, "toWords ")
  /** Examle of composition */
  val process: String => Writer[List[String]] = upCase >=> toWords
}
