package lisp

object Syntax {

  private val arithmeticOperators = List("+")
  private val logicOperators = List("and")
  private val conditionOperators = List("<", ">", "=")
  private val operators = arithmeticOperators ++ logicOperators ++ conditionOperators
  private val codeKeywords = List("if", "set")
  private val keywords = operators ++ codeKeywords

  def isLispBlock(code : String): Boolean = code.startsWith("(") && code.endsWith(")")

  def isIntLeaf(code : String): Boolean = !code.contains(".") && isInt(code)

  def isInt(code : String): Boolean = {
    try {
      code.toInt
      true
    } catch {
      case e : Exception => false
    }
  }

  def isBooleanLeaf(code : String): Boolean = code == "true" || code == "false"

  def isLabelLeaf(code : String): Boolean = !keywords.contains(code)

  def isStatementOperator(code : String): Boolean = operators.contains(code)

  def isArithmeticOperator(code : String): Boolean = arithmeticOperators.contains(code)

  def isLogicOperator(code : String): Boolean = logicOperators.contains(code)

  def isConditionOperator(code : String): Boolean = conditionOperators.contains(code)

  def isCodeKeyword(code : String): Boolean = codeKeywords.contains(code)

  def isKeywords(code : String): Boolean = keywords.contains(code)

  def checkParentheses(code : String): Boolean = {
    if (code.charAt(0) == '(') {
      var i = 0
      for (c <- code.toCharArray) {
        i = countParenthesis(c, i)
        if (i < 0)
          return false
      }
      return i == 0
    }
    false
  }

  def countParenthesis(c : Char, i : Int): Int = {
    if (c == '(')
      i + 1
    else if (c == ')')
      i - 1
    else
      i
  }
}

object Cutter {

  def removeAround(code : String): String =
    code.trim.substring(1, code.length - 1).trim

  def split(code : String): Seq[String] = {
    @annotation.tailrec
    def doLispSplit(code : String, result : Seq[String]): Seq[String] = {
      if (code.length == 0)
        result.reverse
      else {
        val split = if (code.startsWith("(")) cutLispBlock(code) else cutLispLiteral(code)
        doLispSplit(split._2.trim, split._1 +: result)
      }
    }
    doLispSplit(Cutter.removeAround(code), Seq.empty[String])
  }

  def cutLispBlock(code : String): (String, String) = {
    @annotation.tailrec
    def rec(code : String, count : Int, head : String): (String, String) =
      if (count == 0 || code.length == 0)
        (head, code)
      else {
        val c = code.charAt(0)
        rec(code.drop(1), Syntax.countParenthesis(c, count), head + c)
      }
    rec(code.drop(1), 1, code.substring(0, 1))
  }

  def cutLispLiteral(code : String): (String, String) = {
    val cut = code.trim.split(" ")
    (cut.head, code.trim.drop(cut.head.length).trim)
  }
}