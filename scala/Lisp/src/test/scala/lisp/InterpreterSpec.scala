package lisp

import org.scalatest.FunSuite

class InterpreterSpec extends FunSuite {

  test("Interpret add") {
    val interpreter = new Interpreter
    val code = "(+ 1 2 (+ 3 4) 5 (+ 6) (+ 7 (+ 8 9)))"
    val result = interpreter.interpret(code)
    val expected = Right(Integer(45))
    assert(result == expected)
  }

  test("Interpret and") {
    val interpreter = new Interpreter
    val code = "(and true false (and true false) true (and true) (and true (and true true)))"
    val result = interpreter.interpret(code)
    val expected = Right(Bool(false))
    assert(result == expected)
  }

  test("Interpret equal") {
    val interpreter = new Interpreter
    val code = "(= (and false (= (+ 2 2) (+ 2 (+ 1 1)))) (= 1 false))"
    val result = interpreter.interpret(code)
    val expected = Right(Bool(true))
    assert(result == expected)
  }

  test("Interpret if") {
    val interpreter = new Interpreter
    val code = "(if (= 1 3) (+ 2  1) 1)"
    val result = interpreter.interpret(code)
    val expected = Right(Integer(1))
    assert(result == expected)
  }

  test("Interpret set") {
    val interpreter = new Interpreter

    val setTrue = "(set x true)"
    val setFalse = "(set x false)"
    val ifCode = "(if x 1 2)"

    val result1 = interpreter.interpret(setTrue)
    val result2 = interpreter.interpret(ifCode)
    val result3 = interpreter.interpret(setFalse)
    val result4 = interpreter.interpret(ifCode)

    val expected1 = Right(Void())
    val expected2 = Right(Integer(1))
    val expected3 = Right(Void())
    val expected4 = Right(Integer(2))

    assert(result1 == expected1)
    assert(result2 == expected2)
    assert(result3 == expected3)
    assert(result4 == expected4)
  }

  test("Integration test") {
    val interpreter = new Interpreter

    val setX = "(set x 4)"
    val setYFalse = "(set y (= (+ 2 2) (and true false)))"
    val setFOne = "(set y 1)"
    val code = "(if (= (and true false) y) (+ 1 x) (+ x 5))"

    interpreter.interpret(setX)
    interpreter.interpret(setYFalse)
    val result1 = interpreter.interpret(code)

    interpreter.interpret(setFOne)
    val result2 = interpreter.interpret(code)

    val expected1 = Right(Integer(5))
    val expected2 = Right(Integer(9))

    assert(result1 == expected1)
    assert(result2 == expected2)
  }
}
