package lisp

import org.scalatest.FunSuite

class EncoderSpec extends FunSuite {

  test("Add tree must have integer or arithmetic tree as arguments") {
    assert(Encoder.encode(Parser.parse("(+ 2 x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(+ (+ 4) x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(+ true 1)").toOption.get).isLeft)
    assert(Encoder.encode(Parser.parse("(+ (= true false) 1)").toOption.get).isLeft)
    assert(Encoder.encode(Parser.parse("(+ (and true true) x y)").toOption.get).isLeft)
  }

  test("And tree must not have integer and arithmetic tree as arguments") {
    assert(Encoder.encode(Parser.parse("(and true x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(and (= true false) true)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(and (+ 4) x y)").toOption.get).isLeft)
    assert(Encoder.encode(Parser.parse("(and true 1)").toOption.get).isLeft)
  }

  test("If tree must not have integer or arithmetic tree as condition") {
    assert(Encoder.encode(Parser.parse("(if x y z)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(if true x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(if (= 1 4) x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(if (and true false) x y)").toOption.get).isRight)
    assert(Encoder.encode(Parser.parse("(if 2 x y)").toOption.get).isLeft)
    assert(Encoder.encode(Parser.parse("(if (+ 4) x y)").toOption.get).isLeft)
  }
}
