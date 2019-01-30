package monads

import scalaz._
import Scalaz._

/**
 * Created by Yuichiro on 2015/01/26.
 */
object ScratchStateMonad0 {

  type Cache = Map[String, Int]
  val empty = Map.empty[String, Int]

  def main(args: Array[String]) {

    println(incHoge.exec(empty))
    println(incHoge.exec(Map("hoge"->3)))

    println(incHoge(empty))

    val input = List("a", "aaa", "a", "aa", "aa", "a", "aaa", "a")
    println(countUp(input))
    println(countUp2(input))
    println(countUp3(input))
  }

  def incHoge() = for {
    cache <- State.init[Cache]
    hoge <- State((cache: Cache) => (cache, cache.getOrElse("hoge", 0)))
    _ <- State.modify[Cache](_.updated("hoge", hoge + 1))
  } yield ()

  def incHoge(cache: Cache): Cache = {
    val hoge = cache.getOrElse("hoge", 0)
    cache.updated("hoge", hoge + 1)
  }

  def incStr(str: String) = for {
    cache <- State.init[Cache]
    i <- State((cache: Cache) => (cache, cache.getOrElse(str, 0)))
    _ <- State.modify[Cache](_.updated(str, i + 1))
  } yield ()

  def flushStr(str: String) = for {
    cache <- State.init[Cache]
    _ <- State.modify[Cache](_.updated(str, 0))
  } yield ()

  def countUp(strls: List[String]): Cache = {
    var cache = empty
    strls foreach {
      str => cache = incStr(str).exec(cache)
    }
    cache
  }

  def countUp2(strls: List[String]): Cache = {
    def rec(ls: List[String], cache: Cache): Cache = {
      ls match {
        case Nil => cache
        case (head :: tail) => rec(tail, incStr(head).exec(cache))
      }
    }
    rec(strls, empty)
  }

  def countUp3(strls: List[String]): Cache = {
    var cache = empty
    for(str <- strls) {
      cache = incStr(str).exec(cache)
    }
    cache
  }

  def scratch(): Unit = {
    val state = State[Cache, Int] {
      cache => (cache, 1)
    }
    println(state.eval(empty))
    println(state.exec(empty))
    println(state.run(empty))

    println(state.lift.run(empty))

    val state2 = State.constantState(3, empty)
    println(state2.exec(empty))
    println(state2.exec(Map("hoge"->2)))

    val state3 = State.get[Cache]
    println(state3.eval(empty))
    println(state3.eval(Map("hoge"->3)))

    val state4 = State.gets((cache:Cache) => cache.getOrElse("hoge", 1))
    println(state4.eval(empty))
    println(state4.eval(Map("hoge"->10)))

    val state5 = State.init[Cache]
    println(state5.run(empty))

    val state6 = State.modify[Cache](cache => cache.updated("hoge", 3))
    println(state6.run(empty))
    println(state6.run(Map("hoge"->1)))

    val state7 = State.put(Map("hoge" -> 13))
    println(state7.run(empty))

    val state8 = State.apply((cache: Cache) => (cache,()))
    println(state8.run(empty))
  }
}
