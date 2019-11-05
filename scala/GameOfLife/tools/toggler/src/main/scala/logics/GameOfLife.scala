package logics

import scala.util.Random

class GameOfLife(val xSize: Int, val ySize: Int) {

  private var cells = Array.ofDim[Int](xSize, ySize)

  def toggleAlive(x: Int, y: Int): Unit = {
    cells(x % xSize)(y % ySize) += GameOfLife.ALIVE
    cells(x % xSize)(y % ySize) %= 2
  }

  def evolve(): Unit = {
    val newCells = Array.ofDim[Int](xSize, ySize)
    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        val c = countAlive(x, y)
        if (cells(x)(y) == GameOfLife.ALIVE) {
          if (c == 2 || c == 3) {
            newCells(x)(y) = GameOfLife.ALIVE
          }
        } else {
          if (c == 3) {
            newCells(x)(y) = GameOfLife.ALIVE
          }
        }
      }
    }
    cells = newCells
  }

  def clear(): Unit = {
    cells = Array.ofDim[Int](xSize, ySize)
  }

  def randomize(fillRate: Double): Unit = {
    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        if (Random.nextDouble() < fillRate) {
          cells(x)(y) = GameOfLife.ALIVE
        } else {
          cells(x)(y) = GameOfLife.DEAD
        }
      }
    }
  }

  private def countAlive(x: Int, y: Int): Int = {
    cells((x - 1 + xSize) % xSize)((y - 1 + ySize) % ySize) +
      cells((x - 1 + xSize) % xSize)(y) +
      cells((x - 1 + xSize) % xSize)((y + 1) % ySize) +
      cells(x)((y - 1 + ySize) % ySize) +
      cells(x)((y + 1) % ySize) +
      cells((x + 1) % xSize)((y - 1 + ySize) % ySize) +
      cells((x + 1) % xSize)(y) +
      cells((x + 1) % xSize)((y + 1) % ySize)
  }

  def getCells: Array[Array[Int]] = {
    cells
  }

  def setCells(cells: Array[Array[Int]]): Unit = {
    this.cells = cells
  }

  def copy(): GameOfLife = {
    val c = new GameOfLife(xSize, ySize)
    c.setCells(cells.clone)
    c
  }
}

object GameOfLife {

  val DEAD = 0
  val ALIVE = 1
}
