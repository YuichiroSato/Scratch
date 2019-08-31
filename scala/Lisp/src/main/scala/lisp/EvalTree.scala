package lisp

case class Label(label: String)

sealed trait TypedVal {
  def value: AnyRef
  def asScala[T]: T = {
    value match {
      case i: Integer => i.v.asInstanceOf[T]
      case b: Bool => b.v.asInstanceOf[T]
    }
  }

  def toEvalTree: EvalTree = {
    value match {
      case i: Integer => EvalInteger(i)
      case b: Bool => EvalBool(b)
    }
  }
}

case class Bool(v: Boolean) extends TypedVal {
  val value: Bool = this
}

case class Integer(v: Int) extends TypedVal{
  val value: Integer = this
}

case class Void() extends TypedVal {
  val value: Void = this
}

trait EvalTree {
  def eval(env : Environment): TypedVal
}

case class EvalBool(value: Bool) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    value
  }
}

case class EvalInteger(value: Integer) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    value
  }
}

case class EvalVariable(label: Label) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    env.getValue(label).eval(env)
  }
}

case class EvalAdd(args: Seq[EvalTree]) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    Integer(args.map(_.eval(env)).map(_.asScala[Int]).sum)
  }
}

case class EvalAnd(args: Seq[EvalTree]) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    Bool(args.map(_.eval(env)).map(_.asScala[Boolean]).forall(v => v))
  }
}

case class EvalEqual(left: EvalTree, right: EvalTree) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    val l = left.eval(env).value
    val r = right.eval(env).value
    Bool(l == r)
  }
}

case class EvalIf(condition: EvalTree, trueBody: EvalTree, falseBody: EvalTree) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    if (condition.eval(env).asScala[Boolean]) {
      trueBody.eval(env)
    } else {
      falseBody.eval(env)
    }
  }
}

case class EvalSet(label: Label, value: EvalTree) extends EvalTree {
  def eval(env: Environment): TypedVal = {
    env.put(label, value.eval(env).toEvalTree)
    Void()
  }
}