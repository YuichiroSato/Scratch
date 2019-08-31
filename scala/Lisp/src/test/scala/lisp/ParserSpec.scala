package lisp

import org.scalatest.FunSuite

class ParserSpec extends FunSuite {

  test("Parse add tree") {
    val code = "(+ 1 3 5)"
    val expected = AddParseTree(Seq(IntegerLeaf(1), IntegerLeaf(3), IntegerLeaf(5)))
    val result = Parser.entry(code).toOption.get
    assert(expected == result)
  }

  test("Parse and tree") {
    val code = "(and true false true)"
    val expected = AndParseTree(Seq(BoolLeaf(true), BoolLeaf(false), BoolLeaf(true)))
    val result = Parser.entry(code).toOption.get
    assert(expected == result)
  }

  test("Parse equal tree") {
    val code = "(= true false)"
    val expected = EqualParseTree(BoolLeaf(true), BoolLeaf(false))
    val result = Parser.entry(code).toOption.get
    assert(expected == result)
  }

  test("Parse if tree") {
    val code = "(if (= (and true false) false) (+ 1 3) (+ 3 5))"
    val expected = IfParseTree(
      EqualParseTree(AndParseTree(Seq(BoolLeaf(true), BoolLeaf(false))), BoolLeaf(false)),
      AddParseTree(Seq(IntegerLeaf(1), IntegerLeaf(3))),
      AddParseTree(Seq(IntegerLeaf(3), IntegerLeaf(5))))
    val result = Parser.entry(code).toOption.get
    assert(expected == result)
  }

  test("Parse set tree") {
    val code = "(set x 1)"
    val expected = SetParseTree("x", IntegerLeaf(1))
    val result = Parser.entry(code).toOption.get
    assert(expected == result)
  }
}