package lisp

object Util {
  def collectError[T, S](seq: Seq[Either[String, S]]): Either[String, T] = {
    val errors = seq
      .filter(_.isLeft)
      .foldLeft("")((acc, l) => acc + s"${l.left.getOrElse("")}\n")
    Left(errors)
  }
}
