package lisp


class Environment(val previous : Environment) {
  var env = Map.empty[Label, EvalTree]

  def getValue(label : Label): EvalTree = {
    env.getOrElse(label, None) match {
      case exp : EvalTree => exp
      case None => previous.getOption(label) match {
        case Some(exp : EvalTree) => exp
        case None => throw new NoVariableException(label.label + " is not defined")
      }
    }
  }

  def getOption(label : Label): Option[EvalTree] =
    env.getOrElse(label, None) match {
      case exp : EvalTree => Some(exp)
      case None => previous.getOption(label)
    }

  def put(label : Label, value : EvalTree): Unit = {
    env += (label -> value)
  }

  def contains(label : Label): Boolean = previous.contains(label)

  def put(label : String, value : EvalTree): Unit = {
    put(Label(label), value)
  }

  def refresh(): Void = {
    env = Map.empty[Label, EvalTree]
    previous.refresh()
  }

  override def toString: String = {
    env.toString
  }
}

object Environment {
  
  def empty: Environment = new Environment(new NullEnvironment())
}

class NullEnvironment extends Environment(null) {

  override def getValue(label : Label): EvalTree = throw new NoVariableException("NullEnvironment has no variable")
  override def getOption(label : Label): Option[EvalTree] = None
}

class NoVariableException(message : String, cause : Throwable = null) extends RuntimeException(message, cause)