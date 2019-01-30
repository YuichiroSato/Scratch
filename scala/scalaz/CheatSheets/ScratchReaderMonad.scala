package monads

import scalaz._
import Scalaz._

/**
 * Created by Yuichiro on 2015/01/27.
 */
object ScratchReaderMonad {

  def main(args: Array[String]) {
    val r = for {
      a <- (_:Int) + 4
      b <- (_:Int) + 3
    } yield a+b

    println(r(3))

    val f1 = (i: Int) => (i + 2) * (i + 1) % 3
    val r1 = for {
      a <- (_: Int) + 2
      b <- (_: Int) + 1
    } yield a * b % 3

    println(((0 to 100).toList map (f1 &&& r1)).forall(t => t._1 == t._2))

    val f2 = (i: Int) => (i * 2) - (i + 3)
    val r2 = for {
      c <- (_: Int) * 2
      d <- (_: Int) + 3
    } yield c - d

    val f3 = f1 >>> f2
    val r3 = r1 >>> r2

    println(((0 to 100).toList map (f3 &&& r3)).forall(t => t._1 == t._2))

    val r4 = for {
      i <- r1
    } yield r2(i)

    println(((0 to 100).toList map (r3 &&& r4)).forall(t => t._1 == t._2))
  }
}
