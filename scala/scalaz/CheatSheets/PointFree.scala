package functions

/**
 * Created by Yuichiro on 2015/01/12.
 */

import scalaz._
import Scalaz._

object PointFree {

  def main(args: Array[String]) {

    tupleAsTmp()

  }

  def tupleAsTmp(): Unit = {

    val t = ("a", 1)
    val f = (t: (String, Int)) => if (t._1.size == t._2) (t._1 + t._2.toString, t._2) else t
    val tmp = t |> f
    val result = tmp == t
    println(result)

    val makeTmp = (t: (String, Int)) => (t, t)
    val i = (t: (String, Int)) => t
    val comp = (tt: ((String, Int), (String, Int))) => (tt._1 == tt._2, tt._2)
    val rmTmp = (t: (Boolean, (String, Int))) => t._1
    val result2 = makeTmp >>> (f *** i) >>> comp >>> rmTmp
    t |> result2 >>> println
  }
}
