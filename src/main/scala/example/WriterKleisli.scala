package category.example

object WriterKleisliExample {

  import category.data.StringWriterKleisli._

  /** Example functions */
  val upCase:  String => Writer[String]       = s => ("upCase ", s.toUpperCase)
  val toWords: String => Writer[List[String]] = s => ("toWords ", s.split(' ').toList)
  /** Examle of composition */
  val process: String => Writer[List[String]] = upCase >=> toWords
}
