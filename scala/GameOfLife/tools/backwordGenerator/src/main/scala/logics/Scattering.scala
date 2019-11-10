package logics

import scala.util.Random

class Scattering() {

  private val scattering = Array.ofDim[Int](3, 3)

  def randomize(aliveCount: Int): Unit = {
    val _aliveCount = aliveCount % 9
    val arr = Array.ofDim[Int](8)

    for (c <- 0 until _aliveCount) {
      val i = Random.nextInt(8 - c)
      var deadCount = 0
      var j = -1
      while (deadCount <= i) {
        j += 1
        if (arr(j) == Cells.DEAD) {
          deadCount += 1
        }
      }
      arr(j) = Cells.ALIVE
    }

    scattering(0)(0) = arr(0)
    scattering(0)(1) = arr(1)
    scattering(0)(2) = arr(2)
    scattering(1)(0) = arr(3)
    scattering(1)(2) = arr(4)
    scattering(2)(0) = arr(5)
    scattering(2)(1) = arr(6)
    scattering(2)(2) = arr(7)
  }

  def addAlive(n: Int): Unit = {
    val c = countAlive
    var i = 0
    val arr = serialize
    if (c < 8) {
      while (i < n && (c + i) < 8) {
        val x = Random.nextInt(8)
        if (arr(x) == Cells.DEAD) {
          arr(x) = Cells.ALIVE
          i += 1
        }
      }
    }
    allocate(arr)
  }

  def countAlive: Int = {
    scattering(0)(0) +
      scattering(0)(1) +
      scattering(0)(2) +
      scattering(1)(0) +
      scattering(1)(2) +
      scattering(2)(0) +
      scattering(2)(1) +
      scattering(2)(2)
  }

  def setCell(cell: Int, x: Int, y: Int): Unit = {
    scattering(x % 3)(x % 3) = cell
  }

  def setAlive(x: Int, y: Int): Unit = {
    scattering(x % 3)(y % 3) = Cells.ALIVE
  }

  def getScattering: Array[Array[Int]] = {
    scattering
  }

  def allocate(arr: Array[Int]): Unit = {
    scattering(0)(0) = arr(0)
    scattering(0)(1) = arr(1)
    scattering(0)(2) = arr(2)
    scattering(1)(0) = arr(3)
    scattering(1)(2) = arr(4)
    scattering(2)(0) = arr(5)
    scattering(2)(1) = arr(6)
    scattering(2)(2) = arr(7)
  }

  def serialize: Array[Int] = {
    val arr = Array.ofDim[Int](8)
    arr(0) = scattering(0)(0)
    arr(1) = scattering(0)(1)
    arr(2) = scattering(0)(2)
    arr(3) = scattering(1)(0)
    arr(4) = scattering(1)(2)
    arr(5) = scattering(2)(0)
    arr(6) = scattering(2)(1)
    arr(7) = scattering(2)(2)
    arr
  }
}
