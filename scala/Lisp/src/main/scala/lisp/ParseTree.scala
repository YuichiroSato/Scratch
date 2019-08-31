package lisp


sealed trait ParseTree

sealed trait Leaf extends ParseTree

case class IntegerLeaf(value: Int) extends Leaf
case class BoolLeaf(value: Boolean) extends Leaf
case class VariableLeaf(label: String) extends Leaf

class StatementParseTree extends ParseTree

sealed trait ArithmeticParseTree extends StatementParseTree
case class AddParseTree(args: Seq[ParseTree]) extends ArithmeticParseTree

sealed trait LogicParseTree extends StatementParseTree
case class AndParseTree(args: Seq[ParseTree]) extends LogicParseTree

sealed trait ConditionParseTree extends StatementParseTree
case class EqualParseTree(left: ParseTree, rigth: ParseTree) extends ConditionParseTree

sealed trait CodeParseTree extends ParseTree
case class IfParseTree(condition : ParseTree, trueBody : ParseTree, falseBody : ParseTree) extends CodeParseTree
case class SetParseTree(label : String, value : ParseTree) extends CodeParseTree
