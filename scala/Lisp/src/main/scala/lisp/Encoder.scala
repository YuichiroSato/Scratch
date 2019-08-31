package lisp

object Encoder {

  def encode(parseTree: ParseTree): Either[String, EvalTree] =
    parseTree match {
      case l : Leaf => encodeLeaf(l)
      case s : StatementParseTree => encodeStatement(s)
      case c : CodeParseTree => encodeCode(c)
      case _ => Left(s"$parseTree is not supported.")
    }

  def encodeLeaf(leaf: Leaf) : Either[String, EvalTree] =
    leaf match {
      case IntegerLeaf(value) => Right(EvalInteger(Integer(value)))
      case BoolLeaf(value) => Right(EvalBool(Bool(value)))
      case VariableLeaf(label) => Right(EvalVariable(Label(label)))
      case _ => Left(s"$leaf is not supported.")
    }

  def encodeStatement(statement: StatementParseTree): Either[String, EvalTree] =
    statement match {
      case a : ArithmeticParseTree => encodeArithmetic(a)
      case l : LogicParseTree => encodeLogic(l)
      case c : ConditionParseTree => encodeCondition(c)
      case _ => Left(s"$statement is not supported.")
    }

  def encodeArithmetic(arithmetic: ArithmeticParseTree): Either[String, EvalTree] =
    arithmetic match {
      case AddParseTree(args) =>
        val trees = args.map(encode)
        if (trees.exists(_.isLeft)) {
          Util.collectError(trees)
        } else {
          val codes = trees.map(_.toOption.get)
          if (codes.exists(c => !isArithmeticType(c))) {
            Left(s"Type error, [$arithmetic]")
          } else {
            Right(EvalAdd(codes))
          }
        }
      case _ =>  Left(s"$arithmetic is not supported.")
    }

  def isArithmeticType(tree: EvalTree): Boolean =
    tree.isInstanceOf[EvalAdd] || tree.isInstanceOf[EvalInteger] || tree.isInstanceOf[EvalVariable]

  def encodeLogic(logic: LogicParseTree): Either[String, EvalTree] =
    logic match {
      case AndParseTree(args) =>
        val trees =  args.map(encode)
        if (trees.exists(_.isLeft)) {
          Util.collectError(trees)
        } else {
          val codes = trees.map(_.toOption.get)
          if (codes.exists(c => !isLogicType(c))) {
            Left(s"Type error, [$logic]")
          } else {
            Right(EvalAnd(codes))
          }
        }
      case _ =>  Left(s"$logic is not supported.")
    }

  def isLogicType(tree: EvalTree): Boolean =
    tree.isInstanceOf[EvalAnd] || tree.isInstanceOf[EvalEqual] || tree.isInstanceOf[EvalBool] || tree.isInstanceOf[EvalVariable]

  def encodeCondition(condition: ConditionParseTree): Either[String, EvalTree] =
    condition match {
      case EqualParseTree(left, right)=>
        for {
          l <- encode(left)
          r <- encode(right)
        } yield EvalEqual(l, r)
      case _ =>  Left(s"$condition is not supported.")
    }


  def encodeCode(code: CodeParseTree): Either[String, EvalTree] =
    code match {
      case IfParseTree(condition, trueBody, falseBody) =>
        (for {
          c <- encode(condition)
          t <- encode(trueBody)
          f <- encode(falseBody)
        } yield EvalIf(c, t, f))
          .flatMap(evalIf =>
            if (isLogicType(evalIf.condition))
              Right(evalIf)
            else
              Left(s"sType error, [${evalIf.condition}]"))
      case SetParseTree(label, value) =>
        for {
          v <- encode(value)
        } yield EvalSet(Label(label), v)
      case _ => Left(s"$code is not supported.")
    }
}