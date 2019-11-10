package logics

import scala.util.Random

class Cells(xSize: Int, ySize: Int) {

  private var cells = Array.ofDim[Int](xSize, ySize)

  def toggleAlive(x: Int, y: Int): Unit = {
    cells(x % xSize)(y % ySize) += Cells.ALIVE
    cells(x % xSize)(y % ySize) %= 2
  }

  def overlayAlive(cell: Int, x: Int, y: Int): Unit = {
    val cx = cellX(x)
    val cy = cellY(y)
    if (!(cells(cx)(cy) == Cells.DEAD && cell == Cells.DEAD)) {
      cells(cx)(cy) = Cells.ALIVE
    }
  }

  def clear(): Unit = {
    cells = Array.ofDim[Int](xSize, ySize)
  }

  def randomize(fillRate: Double): Unit = {
    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        if (Random.nextDouble() < fillRate) {
          cells(x)(y) = Cells.ALIVE
        } else {
          cells(x)(y) = Cells.DEAD
        }
      }
    }
  }

  def setAlive(x: Int, y: Int): Unit = {
    cells(x)(y) = Cells.ALIVE
  }

  def setDead(x: Int, y: Int): Unit = {
    cells(x)(y) = Cells.DEAD
  }

  def isAlive(x: Int, y: Int): Boolean = {
    cells(x)(y) == Cells.ALIVE
  }

  def isDead(x: Int, y: Int): Boolean = {
    cells(x)(y) == Cells.DEAD
  }

  def countAlive(x: Int, y: Int): Int = {
    val ux = cellX(x - 1)
    val bx = cellX(x + 1)
    val cx = cellX(x)
    val cy = cellY(y)
    val ly = cellY(y - 1)
    val ry = cellY(y + 1)

    cells(ux)(ly) +
      cells(ux)(cy) +
      cells(ux)(ry) +
      cells(cx)(ly) +
      cells(cx)(ry) +
      cells(bx)(ly) +
      cells(bx)(cy) +
      cells(bx)(ry)
  }

  private def cellX(x: Int): Int = {
    (x + xSize) % xSize
  }

  private def cellY(y: Int): Int = {
    (y + ySize) % ySize
  }

  def getCells: Array[Array[Int]] = {
    cells
  }

  def setCells(cells: Array[Array[Int]]): Unit = {
    this.cells = cells
  }

  def allocate(scattering: Scattering, x: Int, y: Int): Unit = {
    val ux = cellX(x - 1)
    val bx = cellX(x + 1)
    val cx = cellX(x)
    val cy = cellY(y)
    val ly = cellY(y - 1)
    val ry = cellY(y + 1)
    val s = scattering.getScattering

    overlayAlive(s(0)(0), ux, ly)
    overlayAlive(s(0)(1), ux, cy)
    overlayAlive(s(0)(2), ux, ry)
    overlayAlive(s(1)(0), cx, ly)
    overlayAlive(s(1)(2), cx, ry)
    overlayAlive(s(2)(0), bx, ly)
    overlayAlive(s(2)(1), bx, cy)
    overlayAlive(s(2)(2), bx, ry)
  }

  def getScattering(x: Int, y: Int): Scattering = {
    val s = new Scattering()
    val ux = cellX(x - 1)
    val bx = cellX(x + 1)
    val cx = cellX(x)
    val cy = cellY(y)
    val ly = cellY(y - 1)
    val ry = cellY(y + 1)

    s.setCell(cells(ux)(ly), 0, 0)
    s.setCell(cells(ux)(cy), 0, 1)
    s.setCell(cells(ux)(ry), 0, 2)
    s.setCell(cells(cx)(ly), 1, 1)
    s.setCell(cells(cx)(ry), 1, 2)
    s.setCell(cells(bx)(ly), 2, 0)
    s.setCell(cells(bx)(cy), 2, 1)
    s.setCell(cells(bx)(ry), 2, 2)

    s
  }
}

object Cells {

  val DEAD = 0
  val ALIVE = 1
}
