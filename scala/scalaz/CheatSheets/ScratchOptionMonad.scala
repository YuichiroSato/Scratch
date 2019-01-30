package monads

/**
 * Created by Yuichiro on 2015/01/24.
 */

import scalaz._
import Scalaz._

object ScratchOptionMonad {

  def main(args: Array[String]) {

    val s = 3.some ? 1 | 2
    println(s)
    val s1 = None ? 1 | 2
    println(s1)


  }
}
