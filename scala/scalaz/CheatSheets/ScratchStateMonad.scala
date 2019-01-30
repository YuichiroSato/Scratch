package monads

/**
 * Created by Yuichiro on 2015/01/12.
 */

import scalaz._
import Scalaz._

object ScratchStateMonad {

  def main(args: Array[String]) {
    val labelmaker = labelMaker("hoge")
    for (_ <- 0 to 3) { println(labelmaker()) }
    println

    val labelmaker2 = getNewLabel("hoge")
    for (i <- 0 to 3) { println(labelmaker2()) }
    println

    val statelabelmaker = getNewLabel2("hoge")
    for (i <- 0 to 3) { println(statelabelmaker()) }
    println

    val multi = multiLabelMaker("hoge", "x", "body")
    for (i <- 0 to 3) { println(multi("hoge")  + " "+ multi("x") + " " + multi("body") + " " + multi("hogehoge"))}
    println

    val multi2 = multiLabelMaker2("hoge", "x")
    for (i <- 0 to 3) { println(multi2("hoge")  + " "+ multi2("x") + " " + multi2("body") + " " + multi2("hogehoge"))}
    println

    val multi3 = multiLabelMaker3("hoge", "x")
    for (i <- 0 to 3) { println(multi3("hoge")  + " "+ multi3("x") + " " + multi3("body") + " " + multi3("hogehoge"))}
  }

  def labelMaker(label: String): () => String = {
    var i = -1
    () => {
      i += 1
      label + i.toString
    }
  }

  val labelMaker2 = (label: String) => (i: Int) => label + i.toString

  val labelMaker3 = (label: String, i: Int) => label + i.toString

  def stateLabelMaker(label: String) = State[Int, String] {
    i => (i + 1, label + i.toString)
  }

  def getNewLabel(label: String): () => String = {
    var i = -1
    val labelmaker = labelMaker2(label)
    () => {
      i += 1
      labelmaker(i)
    }
  }

  def getNewLabel2(label: String): () => String = {
    var i = 0
    val labelmaker = stateLabelMaker(label)
    () => {
      val (c, l) = labelmaker.run(i)
      i = c
      l
    }
  }

  def multiLabelMaker(labels: String*): String => String = {
    var labelMap = (labels.toList map (str => str -> 0)).toMap[String, Int]
    (label: String) => {
      if (labelMap.contains(label)) {
        val count = labelMap(label)
        labelMap += label -> (count + 1)
        label + count.toString
      } else {
        labelMap += label -> 1
        label + 0.toString
      }
    }
  }

  def multiLabelMaker2(labels: String*): String => String = {
    var state = makeLabelMakerState(labels)
    (label: String) => {
      val (st, l) = getNewLabel3(label).run(state)
      state = st
      l
    }
  }

  def multiLabelMaker3(labels: String*): String => String = {
    type Buffer = ((LabelMakerState, String) => String, () => LabelMakerState)
    val buffer = (init: LabelMakerState) => {
      var tmp = init
      ((s: LabelMakerState, l: String) => { tmp = s; l }, () => { tmp })
    }
    val init = (labels: Seq[String]) => buffer(makeLabelMakerState(labels))
    val run = (label: String) => (state: LabelMakerState) => if (state.contains(label)) makeLabel(state, label) else pushTo(state, label)
    val makeLabeler = (buf: Buffer) => buf._2() |> run(_: String) >>> buf._1.tupled
    labels |> init >>> makeLabeler
  }

  type LabelMakerState= Map[String, Int]

  def makeLabelMakerState(labels: Seq[String]): LabelMakerState = {
    labels map (str => str -> 0) toMap
  }

  def getNewLabel3(label: String) = for {
    _ <- State.init[LabelMakerState]
    c <- getLabel3(label)
    l <- branch(label, c)
  } yield l

  def getLabel(label: String) = State[LabelMakerState, String] {
    (state: LabelMakerState) => if (state.contains(label)) makeLabel(state, label) else pushTo(state, label)
  }

  def getLabel2(label: String) = State[LabelMakerState, String] {
    (state: LabelMakerState) =>
      state.get(label) match {
        case Some(_) => makeLabel(state, label)
        case None => pushTo(state, label)
      }
  }

  def getLabel3(label: String) = State[LabelMakerState, Option[Int]] {
    (state: LabelMakerState) => (state, state.get(label))
  }

  def branch(label: String, count: Option[Int]) = State[LabelMakerState, String] {
    (state: LabelMakerState) =>
      count match {
        case Some(_) => makeLabel(state, label)
        case None => pushTo(state, label)
    }
  }

  def makeLabel(state: LabelMakerState, label: String): (LabelMakerState, String) = {
    (state + (label -> (state(label) + 1)), label + state(label))
  }

  def pushTo(state: LabelMakerState, label: String): (LabelMakerState, String) = {
    (state + (label -> 1), label + 0.toString)
  }
}
