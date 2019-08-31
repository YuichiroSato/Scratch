package lisp

import org.scalatest.FunSuite

class EvalTreeSpec extends FunSuite {

  test("Eval integer tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalInteger(Integer(1))
    val expected = Integer(1)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval bool tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalBool(Bool(true))
    val expected = Bool(true)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval variable tree") {
    val env = new Environment(Environment.empty)
    val evalTree1 = EvalSet(Label("x"), EvalBool(Bool(false)))
    val evalTree2 = EvalSet(Label("y"), EvalInteger(Integer(1)))
    evalTree1.eval(env)
    evalTree2.eval(env)

    val variableTree1 = EvalVariable(Label("x"))
    val variableTree2 = EvalVariable(Label("y"))
    val expected1 = Bool(false)
    val expected2 = Integer(1)
    val result1 = variableTree1.eval(env)
    val result2 = variableTree2.eval(env)
    assert(expected1 == result1)
    assert(expected2 == result2)
  }

  test("Eval add tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalAdd(Seq(EvalInteger(Integer(1)), EvalInteger(Integer(2)), EvalInteger(Integer(3))))
    val expected = Integer(6)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval and tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalAnd(Seq(EvalBool(Bool(true)), EvalBool(Bool(true)), EvalBool(Bool(false))))
    val expected = Bool(false)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval equal tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalEqual(EvalBool(Bool(true)), EvalInteger(Integer(2)))
    val expected = Bool(false)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval if tree") {
    val env = new Environment(Environment.empty)
    val condition = EvalEqual(EvalBool(Bool(true)), EvalInteger(Integer(2)))
    val trueBody = EvalAdd(Seq(EvalInteger(Integer(1)), EvalInteger(Integer(2)), EvalInteger(Integer(3))))
    val falseBody = EvalAdd(Seq(EvalInteger(Integer(1)), EvalInteger(Integer(2))))
    val evalTree = EvalIf(condition, trueBody, falseBody)
    val expected = Integer(3)
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval set tree") {
    val env = new Environment(Environment.empty)
    val evalTree = EvalSet(Label("x"), EvalBool(Bool(false)))
    val expected = Void()
    val result = evalTree.eval(env)
    assert(expected == result)
  }

  test("Eval add tree includes variable tree") {
    val env = new Environment(Environment.empty)
    val x = EvalSet(Label("x"), EvalInteger(Integer(3)))
    x.eval(env)

    val evalTree = EvalAdd(Seq(EvalInteger(Integer(1)), EvalInteger(Integer(2)), EvalVariable(Label("x"))))
    val expected = Integer(6)
    val result = evalTree.eval(env)
    assert(expected == result)
  }
}
