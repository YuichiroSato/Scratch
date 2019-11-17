package logics

import scala.util.Random

class GameOfLife(val xSize: Int, val ySize: Int) {

  private var cells = new Cells(xSize, ySize)

  def toggleAlive(x: Int, y: Int): Unit = {
    cells.toggleAlive(x, y)
  }

  def evolve(): Unit = {
    val newCells = new Cells(xSize, ySize)
    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        val c = cells.countAlive(x, y)
        if (cells.isAlive(x, y)) {
          if (c == 2 || c == 3) {
            newCells.setAlive(x, y)
          }
        } else {
          if (c == 3) {
            newCells.setAlive(x, y)
          }
        }
      }
    }
    cells = newCells
  }

  def backward(): Unit = {
    val start = System.currentTimeMillis()
    var bestCells = new Cells(xSize, ySize)
    var bestBroken = countBroken(bestCells)
    var i = 0
    val n = 100000 * 100
    while (i < n && bestBroken != 0) {
      if (i % 1000 == 0) {
        println(s"$i , elapsed timebest: ${(System.currentTimeMillis() - start) / 1000} , $bestBroken")
      }
      val candidate = gen(bestCells)
      val candidateBroken = countBroken(candidate)
      if (candidateBroken < bestBroken || Random.nextDouble() < 1.0 - i / n) {
        bestCells = candidate
        bestBroken = candidateBroken
      }
      i += 1
    }
    val end = System.currentTimeMillis()
    println(s"best: $bestBroken")
    println(s"elapsed time: ${end - start}")
    cells = bestCells
  }

  private def gen(bestCells: Cells): Cells = {
    val newCells = new Cells(xSize, ySize)

    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        if (cells.isAlive(x, y)) {
          val s = bestCells.getScattering(x, y)
          val count = bestCells.countAlive(x, y)
          if (Random.nextDouble() < 0.5) {
            val n = 2 - count
            if (n > 0) {
              s.killCells(1)
              s.addAlive(n + 1)
            } else {
              s.randomize(2)
            }
            newCells.allocate(s, x, y)
            newCells.setAlive(x, y)
          } else {
            val n = 3 - count
            if (n > 0) {
              s.killCells(1)
              s.addAlive(n + 1)
            } else {
              s.randomize(3)
            }
            newCells.allocate(s, x, y)
            if (Random.nextDouble() < 0.5) {
              newCells.setDead(x, y)
            }
          }
        }
      }
    }

    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        if (cells.isDead(x, y) && newCells.isAlive(x, y) && newCells.countAlive(x, y) == 3) {
          val s = newCells.getScattering(x, y)
          if (Random.nextDouble() < 0.5) {
            s.addAlive(1)
          } else {
            s.addAlive(2)
          }
          newCells.allocate(s, x, y)
        }
      }
    }

    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        if (cells.isDead(x, y) && newCells.isAlive(x, y) && newCells.countAlive(x, y) == 3) {
          val s = newCells.getScattering(x, y)
          if (Random.nextDouble() < 0.5) {
            s.addAlive(1)
          } else {
            s.addAlive(2)
          }
          newCells.allocate(s, x, y)
        }
      }
    }

    newCells
  }

  def countBroken(generatedCells: Cells): Int = {
    var brokenCount = 0
    for (x <- 0 until xSize) {
      for (y <- 0 until ySize) {
        val count = generatedCells.countAlive(x, y)
        if (cells.isDead(x, y)&& count == 3) {
          brokenCount += 1
        }
        if (cells.isAlive(x, y) && !(count == 2 || count == 3)) {
          brokenCount += 1
        }
        if (cells.isDead(x, y) && generatedCells.isAlive(x, y) && (count == 2 || count == 3)) {
          brokenCount += 1
        }
        if (cells.isAlive(x, y) && generatedCells.isDead(x, y) && count != 3) {
          brokenCount += 1
        }
      }
    }
    brokenCount
  }

  def clear(): Unit = {
    cells.clear()
  }

  def randomize(fillRate: Double): Unit = {
    cells.randomize(fillRate)
  }

  def getCells: Cells = {
    cells
  }

  def setCells(cells: Cells): Unit = {
    this.cells = cells
  }

  def copy(): GameOfLife = {
    val g = new GameOfLife(xSize, ySize)
    val c = new Cells(xSize, ySize)
    c.setCells(cells.getCells.clone)
    g.setCells(c)
    g
  }
}
