package functions

/**
 * Created by Yuichiro on 2015/01/11.
 */

import scalaz._
import Scalaz._

object Primes {

/*
  Prime square remainders Problem 123
  https://projecteuler.net/problem=123

  Let p_n be the nth prime: 2, 3, 5, 7, 11, ..., and let r be the remainder when (p_n−1)^n + (p_n+1)^n is divided by p_n^2.
  For example, when n = 3, p_3 = 5, and 4^3 + 6^3 = 280 ≡ 5 mod 25.
  The least value of n for which the remainder first exceeds 10^9 is 7037.
  Find the least value of n for which the remainder first exceeds 10^10.
*/

  def main(args: Array[String]) {
    scratch()
  }

  def scratch(): Unit = {
    val checker = (f1: Int => PrimeList, f2: Int => PrimeList) => ((2 to 100) map ((i: Int) => f1(i) == f2(i))).fold(true)(_ && _)

    val primesto1 = (i: Int) => PrimeList.primesTo(i, PrimeList.empty)
    val primesto2 = (i: Int) => PrimeList.primesTo2(i, PrimeList.empty)
    val primesto3 = (i: Int) => PrimeList.primesTo3(i, PrimeList.empty)
    println(checker(primesto1, primesto2))
    println(checker(primesto1, primesto3))

    println(checker(PrimeList.withSize(_), PrimeList.withSize2(_)))

    analyticalSolver2()

  }

  def doSolve(): Unit = {
    prover()

    val start = System.currentTimeMillis()
    analyticalSolver()
    println("analytical solver : " + (System.currentTimeMillis() - start) + "ms")

    val start2 = System.currentTimeMillis()
    simpleSolver()
    println("simple solver : " + (System.currentTimeMillis() - start2) + "ms")
  }

  def analyticalSolver(): Unit = {
    var pls = PrimeList withSize Cons.sampleN
    println("prime " + pls.max + ", n " + pls.maxIndex + " check " + (Cons.sampleLog < Calc.logRemainder(pls.max, pls.maxIndex))) // 71059, 7037, 1000...
    for (i <- (Cons.sampleN + 1) to Int.MaxValue) {
      val buf = pls
      pls = PrimeList.primesTo(i, pls)
      if (buf.maxIndex < pls.maxIndex) {
        val rem = Calc.logRemainder(pls.max, pls.maxIndex)
        if (Cons.targetLog < rem) {
          println("answer prime " + pls.max + ", n " + pls.maxIndex)
          return
        }
        //println("prime" + pls.max + " is not. n " + pls.maxIndex + " rem " + rem)
      }
    }
  }

  def analyticalSolver2(): Unit = {
    type Buffer = PrimeList
    type BufTuple = (Buffer, PrimeList)
    val takeCurrent = (t: BufTuple) => t._2
    val takeBuffer = (t: BufTuple) => t._1
    val pushBuffer = (pls: PrimeList, t: BufTuple) => (t._2, pls)

    val newPrimeIsFound = (t: BufTuple, pls: PrimeList) => (t |> takeBuffer).maxIndex < pls.maxIndex
    val tryFindPrime = (i: Int, pls: PrimeList) => PrimeList.primesTo(i, pls)
    val update = (i: Int, t: BufTuple) => {
      val pls = t |> takeCurrent >>> tryFindPrime.curried(i)
      if (newPrimeIsFound(t, pls))
        pushBuffer(pls, t)
      else
        t
    }

    val checkCondition = (pn: Int, n: Int) => Cons.targetLog < Calc.logRemainder(pn, n)
    val isEnd = (t: BufTuple) => {
      val current = t |> takeCurrent
      checkCondition(current.max, current.maxIndex)
    }
    val isUpdated = (t: BufTuple) => t._1.max != t._2.max

    var buf = (PrimeList.empty, PrimeList(2))
    for (i <- 3 to Int.MaxValue) {
      buf = buf |> update.curried(i)
      if (buf |> isEnd) {
        println("answer prime " + (buf |> takeCurrent).max + ", n " + (buf |> takeCurrent).maxIndex)
        return
      } else if (buf |> isUpdated) {
        println("prime" + (buf |> takeCurrent).max + " is not. n " + (buf |> takeCurrent).maxIndex)
      }
    }
  }

  def analyticalSolver3(): Unit = {
    type Buffer = PrimeList
    type BufTuple = (Buffer, PrimeList)
    val takeCurrent = (t: BufTuple) => t._2
    val takeBuffer = (t: BufTuple) => t._1
    val pushBuffer = (pls: PrimeList, t: BufTuple) => (t._2, pls)

    val newPrimeIsFound = (t: BufTuple, pls: PrimeList) => (t |> takeBuffer).maxIndex < pls.maxIndex
    val updateBuffer = (t: BufTuple, pls: PrimeList) => if (newPrimeIsFound(t, pls)) pushBuffer(pls, t) else t
    val tryFindPrime = (i: Int, pls: PrimeList) => PrimeList.primesTo(i, pls)
    val update = (i: Int, t: BufTuple) => updateBuffer(t, tryFindPrime(i, t._2))

    val checkCondition = (pn: Int, n: Int) => Cons.targetLog < Calc.logRemainder(pn, n)
    val makeInput = (pls: PrimeList) => (pls.max, pls.maxIndex)
    val isEnd: PrimeList => Boolean = makeInput >>> checkCondition.tupled

    val showFinalResult = (pls: PrimeList) => {
      println("answer prime " + pls.max + ", n " + pls.maxIndex)
    }
    val showMiddleResult = (t: BufTuple) => {
      if (t._1.max != t._2.max)
        println("prime" + t._2.max + " is not. n " + t._2.maxIndex)
    }
    val showResult = (t: BufTuple) => if (t |> takeCurrent >>> isEnd) showFinalResult(t |> takeCurrent) else showMiddleResult(t)

    val id = (t: BufTuple) => t
    val removeUnit = (t: (Unit, BufTuple)) => t._2
    val showAsSideEffect: BufTuple => BufTuple = (showResult &&& id) >>> removeUnit

    val loopMain = (i: Int) => (i |> update.curried) >>> showAsSideEffect

    def loop(i: Int, t: BufTuple): Unit = {
      if (Int.MaxValue <= i || (t |> takeCurrent >>> isEnd))
        return
      else
        loop(i + 1, t |> (i |> loopMain))
    }
    loop(3, (PrimeList.empty, PrimeList(2)))
  }

  def prover(): Unit = {
    var pls = PrimeList.empty
    for(i <- 2 to 100) {
      pls = PrimeList.primesTo(i, pls)
      println("prime " + pls.max + " n " + pls.maxIndex)
      println("rem " + Calc.remainder(pls.max, pls.maxIndex) + " analytical " + Calc.analytical(pls.max, pls.maxIndex))
      println(Calc.analytical(pls.max, pls.maxIndex) == Calc.remainder(pls.max, pls.maxIndex))
      println()
    }
  }

  def simpleSolver(): Unit = {
    var pls = PrimeList withSize Cons.sampleN
    println("prime " + pls.max + ", n " + pls.maxIndex + " rem " + Calc.remainder(pls.max, pls.maxIndex)) // 71059, 7037, 1000...
    for (i <- (pls.max + 1) to Int.MaxValue) {
      val buf = pls
      pls = PrimeList.primesTo(i, pls)
      if (buf.maxIndex < pls.maxIndex) {
        val rem = Calc.remainder(pls.max, pls.maxIndex)
        if (Cons.targetBigInt < rem) {
          println("answer prime " + pls.max + ", n " + pls.maxIndex)
          return
        }
        //println("prime" + pls.max + " is not. n " + pls.maxIndex + " rem " + rem)
      }
    }
  }


}

object Cons {

  val sampleN = 7037

  val sampleBigInt = BigInt(1000) * BigInt(1000) * BigInt(1000)
  val targetBigInt = BigInt(10) * sampleBigInt

  val sampleLog = 9.0
  val targetLog = 10.0
}

object Calc {

  def remainder(pn: Int, n: Int): BigInt = (pow(pn - 1, n) + pow(pn + 1, n)).%(pow(pn, 2))

  private def pow(a: BigInt, n: Int): BigInt = a.pow(n)

  def analytical(pn: Int, n: Int): BigInt = {
    n match {
      case 1 => BigInt(0)
      case 3 => BigInt(5)
      case _ if n % 2 == 0 => BigInt(2)
      case _ => BigInt(2) * BigInt(n) * BigInt(pn)
    }
  }

  def logRemainder(pn: Int, n: Int): Double = {
    n match {
      case 1 => Double.MinValue
      case 3 => Math.log10(5)
      case _ if n % 2 == 0 => Math.log10(2)
      case _ => Math.log10(2) + Math.log10(n) + Math.log10(pn)
    }
  }
}
case class PrimeList(pls: List[Int], sorted: Boolean = false) {

  def contains(p : Int): Boolean = pls.contains(p)

  def max: Int = {
    if (pls.size < 1) 0
    else if (sorted) pls.head
    else PrimeList.sort(this).pls.head
  }

  def maxIndex: Int = pls.size
}

object PrimeList {

  def apply(a: Int, xs: Int*): PrimeList = sort(new PrimeList(a :: xs.toList))

  def empty: PrimeList = PrimeList(List.empty[Int], sorted = true)

  def add(p: Int, pls: PrimeList): PrimeList = if (pls.sorted) PrimeList(p :: pls.pls, sorted = true) else PrimeList(p :: sort(pls).pls, sorted = true)

  def sort(pls: PrimeList): PrimeList = PrimeList(pls.pls.sorted, sorted = true)

  def isPrime(n: Int, pls: PrimeList): Boolean = if (n < pls.max) pls.contains(n) else primesTo(n, pls).contains(n)

  def withSize(size: Int): PrimeList = {
    var pls = PrimeList.empty
    var n = 2
    while(pls.maxIndex < size) {
      pls = primesTo(n, pls)
      n += 1
    }
    pls
  }

  def withSize2(size: Int): PrimeList = {
    val maker = primeMaker(PrimeList.empty)
    var n = 2
    while((maker(n) |> getSize) < size) n += 1
    getPrimeList(maker)
  }

  def to(n: Int): PrimeList = primesTo(n, PrimeList.empty)

  def primesTo(n: Int, pls: PrimeList): PrimeList = {
    var pls2 = pls
    val max = if (1 < pls.maxIndex) pls.max else 2
    for (i <- max to n) {
      if (!dividable(i, pls2)) pls2 = add(i, pls2)
    }
    pls2
  }

  def primesTo2(n: Int, pls: PrimeList): PrimeList = {
    val max = (pls : PrimeList) => if (1 < pls.maxIndex) pls.max else 2
    def rec(i: Int, result: PrimeList): PrimeList = {
      if (n < i)
        result
      else
        rec(i + 1, if (!dividable(i, result)) add(i, result) else result)
    }
    rec(pls |> max, pls)
  }

  def primesTo3(n: Int, pls: PrimeList): PrimeList = {
    val maker = primeMaker(pls)
    val max = if (1 < pls.maxIndex) pls.max else 2
    (max to n) foreach maker
    getPrimeList(maker)
  }

  private def dividable(n: Int, pls: PrimeList): Boolean = {
    pls.pls foreach {
      p => if (n % p == 0) return true
    }
    false
  }

  def primeMaker(pls: PrimeList): Int => (PrimeList, Int) = {
    var tmp = pls
    (i: Int) => {
      val check = dividable(i, tmp)
      if (!check) tmp = add(i, tmp)
      (tmp, tmp.maxIndex)
    }
  }

  def getPrimeList(primeMaker: Int => (PrimeList, Int)): PrimeList = primeMaker(0)._1
  def getSize(t: (PrimeList, Int)): Int = t._2
}