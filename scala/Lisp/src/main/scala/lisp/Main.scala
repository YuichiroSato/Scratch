package lisp

import scala.io.Source

object Main {

  def main(args : Array[String]): Unit = {
    val input = Source.fromInputStream(System.in)
    val interpreter = new Interpreter
    var code = ""

    for (line <- input.getLines) {
      if (line == "exit") {
        return
      }
      else {
        code += line
        if (Syntax.checkParentheses(code)) {
          try {
            println(interpreter.interpret(code))
            code = ""
          } catch {
            case e: Exception =>
              println(code)
              e.printStackTrace()
              code = ""
          }
        }
      }
    }
  }
}

class Interpreter {

  val env = new Environment(Environment.empty)

  def interpret(str : String): AnyRef =
    for {
      parseTree <- Parser.entry(str)
      evalTree <- Encoder.encode(parseTree)
    } yield evalTree.eval(env)

  def interpret(exp : EvalTree): AnyRef = exp.eval(env)

  def refresh(): Unit= env.refresh()
}