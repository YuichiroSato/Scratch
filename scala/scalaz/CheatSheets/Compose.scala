package functions

/**
 * Created by Yuichiro on 2015/01/10.
 */

import scalaz._
import Scalaz._

object Compose {

  def main(args: Array[String]) {
    scratch()
    fibonacciScratch()
  }

  def scratch(): Unit = {
    val add1 = (_: Int) + 1
    val mul5 = (_: Int) * 5

    val com0 = mul5 andThen add1
    val com1 = mul5 >>> add1

    val com2 = add1 compose mul5
    val com3 = add1 <<< mul5

    println(com0(13) + " " + com1(13) + " " + com2(13) + " " + com3(13))
    println((13 |> com0) + " " + (13 |> com1) + " " + (13 |> com2) + " " + (13 |> com3))

    val add_mul0 = (i: Int) => (add1(i), mul5(i))
    val add_mul1 = add1 &&& mul5
    println(add_mul0(3) + " " + add_mul1(3))

    val add_mul2 = (t: (Int, Int)) => (add1(t._1), mul5(t._2))
    val add_mul3 = add1 *** mul5
    println(add_mul2((4,2)) + " " + add_mul3((4,2)))

    val left = add1 *> mul5
    println(left(6) + " " + mul5(6))

    val right = add1 <* mul5
    println(right(6) + " " + add1(6))

    val addq = (_:Int).toString + "?"
    val add_add0 = add1 -*- addq
    val add_add1 = add1 *** addq
    println(add_add0((1,4)) + " " + add_add1((1,4)))

    val add_hoge = add1 -> "hoge"
    println(add_hoge._1(4) + " " + add_hoge._2)

    val applicative0 = (i: Int) => i * 300 + add1(i) * 10
    val applicative1 = add1 <*> (i => j => i * 300 + j * 10)
    println(applicative1(2) + " " + applicative0(2))

    val applicative3 = (i: Int) => addq(add1(i)) + i.toString
    val applicative4 = add1 <*> ((i:Int) => addq(_) + i.toString)
    println(applicative4(18) + " " + applicative3(18))

    val add_p_mul0 = (i: Int) => add1(i) + mul5(i)
    val add_p_mul1 = add1 |+| mul5
    println(add_p_mul1(17) + " " + add_p_mul0(17))

    val add_m_mul0 = (i: Int) => add1(i) - mul5(i)
    val add_m_mul1 = add1 |-| mul5
    println(add_m_mul1(4) + " " + add_m_mul0(4))

    val higher = (f: (Int) => Int) => f >>> mul5
    val add1_mul5h = add1 |> higher
    println(add1_mul5h(3))

  }

  def fibonacciScratch(): Unit = {
    for(i <- 0 to 10) { print(" " + fib(i)) }
    println()

    for(i <- 0 to 10) { print(" " + fib2(i)) }
    println()

    for(i <- 0 to 10) { print(" " + fib3(i)) }
    println()

    for(i <- 0 to 10) { print(" " + fib4(i)) }
    println()

    val bi = nib(2, 0, 1)
    for (i <- 0 to 10) { print(" " + bi(i)) }
    println()

    val tri = nib(3, 0, 0, 1)
    for (i <- 0 to 10) { print(" " + tri(i)) }
    println()

    val tri2 = nib2(3, 0, 0, 1)
    for (i <- 0 to 10) { print(" " + tri2(i)) }
    println()

    println("Benchmark")

    val expn = 10000
    val start = System.currentTimeMillis()
    for (i <- 0 to expn) fib(i)
    println(System.currentTimeMillis() - start + "ms")

    val start2 = System.currentTimeMillis()
    for (i <- 0 to expn) fib4(i)
    println(System.currentTimeMillis() - start2 + "ms")

    val start3 = System.currentTimeMillis()
    for (i <- 0 to expn) fib5(i)
    println(System.currentTimeMillis() - start3 + "ms")
  }

  def fib(n: Int): Int = {
    var t = (0,1)
    for (_ <- 1 to n) {
      t = (t._2, t._1 + t._2)
    }
    t._1
  }

  def fib2(n: Int): Int = {
    val getFib = (t: (Int, Int)) => t._1
    val nextFib = (t: (Int, Int)) => t._1 + t._2
    val update = (t: (Int, Int)) => (t._2, nextFib(t))
    def rec(i: Int, result: (Int, Int)): Int = {
      if (i <= 0)
        getFib(result)
      else
        rec(i - 1, update(result))
    }
    rec(n, (0, 1))
  }

  def fib3(n: Int): Int = {
    type Updater = ((Int, Int)) => (Int, Int)
    val getFib = (t: (Int, Int)) => t._1
    val update: Updater = t => (t._2, t._1 + t._2)
    val init: Updater = t => t
    // compose n of component updater to make bigger updater
    def composer(n: Int, component: Updater, result: Updater): Updater = {
      if (n <= 0)
        result
      else
        composer(n - 1, component, result >>> component)
    }
    (0, 1) |> composer(n, update, init) >>> getFib
  }

  def fib4(n: Int): Int = {
    type Updater = ((Int, Int)) => (Int, Int)
    val getFib = (t: (Int, Int)) => t._1
    val update: Updater = t => (t._2, t._1 + t._2)
    val init: Updater = t => t
    // n update functions are folded by andThen
    val comp: Updater = List.fill(n)(update).fold(init)(_ >>> _)
    (0, 1) |> comp >>> getFib
  }

  def fib5(n: Int): Int = {
    type Updater = ((Int, Int)) => (Int, Int)
    val getFib = (t: (Int, Int)) => t._1
    val update: Updater = t => (t._2, t._1 + t._2)
    val init: Updater = t => t
    // n update functions are folded by andThen
    lazy val comp: Updater = List.fill(n)(update).fold(init)(_ >>> _)
    (0, 1) |> comp >>> getFib
  }

  def nib(n: Int, xs: Int*): Int => Int = {
    (m: Int) => {
      var ls = xs.toList.take(n)
      for (_ <- 1 to m) {
        val tmp = ls.fold(0)(_ + _)
        ls = (tmp :: ls).take(n)
      }
      ls.head
    }
  }

  def nib2(n: Int, xs: Int*): Int => Int = {
    val initialList = xs.toList.take(n)
    val calculateNib = (ls: List[Int]) => ls.fold(0)(_ + _)
    val getNib = (ls: List[Int]) => ls.head
    val update = (ls: List[Int]) => (calculateNib(ls) :: ls).take(n)
    val init = (ls: List[Int]) => ls
    val comp = (i: Int) => List.fill(i)(update).fold(init)(_ >>> _)
    initialList |> comp(_: Int) >>> getNib
  }
}
