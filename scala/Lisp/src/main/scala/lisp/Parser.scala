package lisp

object Parser {

  def entry(code: String): Either[String, ParseTree] = {
    if (Syntax.checkParentheses(code))
      parse(code)
    else
      Left(s"Parentheses are broken, [$code]")
  }

  def parse(code: String): Either[String, ParseTree] =
    if (Syntax.isLispBlock(code)) parseNode(Cutter.split(code)) else CodeParser.parseLeaf(code)

  def parseNode(codes: Seq[String]): Either[String, ParseTree] =
    codes match {
      case operator :: tail =>
        if (Syntax.isStatementOperator(operator)) {
          StatementParser.parse(operator, tail)
        } else if (Syntax.isCodeKeyword(operator)) {
          CodeParser.parse(operator, tail)
        } else {
          Left(s"$operator is not supported.")
        }
      case _ => Left(s"Parse error, [$codes]")
    }
}

object StatementParser {

  def parse(operator: String, codes: Seq[String]): Either[String, StatementParseTree] =
    if (Syntax.isArithmeticOperator(operator)) {
      parseArithmetic(operator, codes)
    } else if (Syntax.isLogicOperator(operator)) {
      parseLogic(operator, codes)
    } else if (Syntax.isConditionOperator(operator)) {
      parseCondition(operator, codes)
    } else {
      Left(s"$operator is not supported.")
    }

  def parseArithmetic(operator: String, codes: Seq[String]): Either[String, ArithmeticParseTree] =
    operator match {
      case "+" =>
        val trees = codes.map(Parser.parse)
        if (trees.exists(_.isLeft)) {
          val errors = trees
            .filter(_.isLeft)
            .foldLeft("")((acc, l) => acc + s"${l.left.getOrElse("")}\n")
          Left(errors)
        } else {
          Right(AddParseTree(trees.map(_.toOption.get)))
        }
      case _ => Left(s"$operator is not supported as an arithmetic operator.")
    }

  def parseLogic(operator: String, codes: Seq[String]): Either[String, LogicParseTree] =
    operator match {
      case "and" =>
        val trees = codes.map(Parser.parse)
        if (trees.exists(_.isLeft)) {
          Util.collectError(trees)
        } else {
          Right(AndParseTree(trees.map(_.toOption.get)))
        }
      case _ => Left(s"$operator is not supported as a logic operator.")
    }

  def parseCondition(codes: Seq[String]): Either[String, ConditionParseTree] =
    codes match {
      case operator :: tail => parseCondition(operator, tail)
      case _ => Left(s"Parse error, [$codes]")
    }

  def parseCondition(operator: String, codes: Seq[String]): Either[String, ConditionParseTree] =
    operator match {
      case "=" =>
        codes match {
          case right :: left :: Nil =>
            for {
              r <- Parser.parse(right)
              l <- Parser.parse(left)
            } yield EqualParseTree(r, l)
        }
      case _ => Left(s"$operator is not supported as a condition operator.")
  }
}

object CodeParser {

  def parse(code : String, tail : Seq[String]): Either[String, CodeParseTree] =
    code match {
      case "if" => parseIf(tail)
      case "set" => parseSet(tail)
      case _ => Left(s"$code is not supported.")
    }

  def parseIf(codes : Seq[String]): Either[String, IfParseTree] =
    codes match {
      case condPart :: truePart :: falsePart :: Nil =>
        for {
          c <- Parser.parse(condPart)
          t <- Parser.parse(truePart)
          f <- Parser.parse(falsePart)
        } yield IfParseTree(c, t, f)
      case _ => Left(s"Parse error, [$codes]")
    }

  def parseSet(codes : Seq[String]): Either[String, SetParseTree] =
    codes match {
      case label :: value :: Nil =>
        for {
          v <- Parser.parse(value)
        } yield SetParseTree(label, v)
      case _ => Left(s"Parse error, [$codes]")
    }

  def parseLeaf(code : String): Either[String, ParseTree] =
    if (Syntax.isIntLeaf(code)) {
      Right(IntegerLeaf(code.toInt))
    } else if (Syntax.isBooleanLeaf(code)) {
      Right(BoolLeaf(code.toBoolean))
    } else if (Syntax.isLabelLeaf(code)) {
      Right(VariableLeaf(code))
    } else {
      Left(s"$code is not a Literal.")
    }
}

