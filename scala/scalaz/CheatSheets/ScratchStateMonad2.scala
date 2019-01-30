package monads

/**
 * Created by Yuichiro on 2015/01/23.
 */

import scalaz._
import Scalaz._

object ScratchStateMonad2 {

  def main (args: Array[String]) {
    fizzBuzz()
    println
    fizzBuzz2()
    println
    fizzBuzz3()
  }

  def fizzBuzz(): Unit = {
    var countFizz = 0
    var countBuzz = 0
    for (i <- 0 to 100) {
      print(i + " ")
      if ((countFizz + countBuzz) % 7 == 0) {
        print("fizz buzz")
        countFizz += 1
        countBuzz += 1
      }
      if (i % 3 == 0) {
        print("fizz")
        countFizz += 1
      }
      if (i % 5 == 0) {
        print("buzz")
        countBuzz += 1
      }
      println
    }
  }

  type Fizz = Int
  type Buzz = Int
  type Counter = (Fizz, Buzz)

  val incFizz = (counter: Counter) => (counter._1 + 1, counter._2)
  val incBuzz = (counter: Counter) => (counter._1, counter._2 + 1)
  val incFizzBuzz = (counter: Counter) => counter |> incFizz >>> incBuzz

  def printFizzBuzz = State[Counter, Unit] {
    counter => if ((counter._1 + counter._2) % 7 == 0) (counter |> incFizzBuzz, print("fizz buzz")) else (counter, ())
  }

  def printFizz(i: Int) = State[Counter, Unit] {
    counter => if (i % 3 == 0) (counter |> incFizz, print("fizz")) else (counter, ())
  }

  def printBuzz(i: Int) = State[Counter, Unit] {
    counter => if (i % 5 == 0) (counter |> incBuzz, print("buzz")) else (counter, ())
  }

  def loop(i: Int) = for {
    _ <- printFizzBuzz
    _ <- printFizz(i)
    _ <- printBuzz(i)
  } yield ()

  def fizzBuzz2(): Unit = {
    var state = (0,0)
    for(i <- 0 to 100) {
      print(i + " ")
      state = loop(i).exec(state)
      println
    }
  }

  case class Counter2 (fizz: Int, buzz: Int) {
    def incFizz: Counter2 = copy(fizz = this.fizz + 1)
    def incBuzz: Counter2 = copy(buzz = this.buzz + 1)
    def incFizzBuzz: Counter2 = Counter2(fizz + 1, buzz + 1)
  }

  def printFizzBuzz2(counter: Counter2): Counter2 = {
    if ((counter.fizz + counter.buzz) % 7 == 0) {
      print("fizz buzz")
      counter.incFizzBuzz
    } else {
      counter
    }
  }

  def printFizz2(i: Int, counter: Counter2): Counter2 = {
    if (i % 3 == 0) {
      print("fizz")
      counter.incFizz
    } else {
      counter
    }
  }

  def printBuzz2(i: Int, counter: Counter2): Counter2 = {
    if (i % 5 == 0) {
      print("buzz")
      counter.incBuzz
    } else {
      counter
    }
  }

  def loop2(i: Int, counter: Counter2): Counter2 = {
    printBuzz2(i, printFizz2(i, printFizzBuzz2(counter)))
  }

  def fizzBuzz3(): Unit = {
    var counter = Counter2(0,0)
    for (i <- 0 to 100) {
      print(i + " ")
      counter = loop2(i, counter)
      println
    }
  }
}
