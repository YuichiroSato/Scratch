package annealings

import logics.{Annealing, Cells}

import scala.util.Random

class Annealing0 extends Annealing {

   val name = "annealing0"

   def generate(baseCells: Cells, bestCells: Cells): Cells = {
      val newCells = new Cells(baseCells.xSize, baseCells.ySize)

      for (x <- 0 until baseCells.xSize) {
         for (y <- 0 until baseCells.ySize) {
            if (baseCells.isAlive(x, y)) {
               val s = bestCells.scattering(x, y)
               val count = bestCells.countAlive(x, y)
               if (Random.nextDouble() < 0.5) {
                 val n = 2 - count
                 if (n > 0) {
                   s.killCells(1)
                   s.addAlive(n + 1)
                 } else {
                   s.randomize(2)
                 }
                 newCells.overlay(s, x, y)
                 val newScattering = newCells.scattering(x, y)
                 val newCount = newScattering.countAlive
                 if (newCount > 2) {
                   newScattering.killCells(newCount - 2)
                 }
                 newCells.allocate(newScattering, x, y)
                 newCells.setAlive(x, y)
               } else {
                  val n = 3 - count
                  if (n > 0) {
                     s.killCells(1)
                     s.addAlive(n + 1)
                  } else {
                     s.randomize(3)
                  }
                  newCells.overlay(s, x, y)
                  if (Random.nextDouble() < 0.5) {
                     newCells.setDead(x, y)
                  }
                 val newScattering = newCells.scattering(x, y)
                 val newCount = newScattering.countAlive
                 if (newCount > 3) {
                   newScattering.killCells(newCount - 3)
                 }
                 newCells.allocate(newScattering, x, y)
               }
            }
         }
      }

      newCells
   }

   def countBroken(baseCells: Cells, generatedCells: Cells): Double = {
      var brokenCount = 0
      for (x <- 0 until baseCells.xSize) {
         for (y <- 0 until baseCells.ySize) {
            val count = generatedCells.countAlive(x, y)
            if (baseCells.isDead(x, y)&& count == 3) {
               brokenCount += 1
            }
            if (baseCells.isAlive(x, y) && !(count == 2 || count == 3)) {
               brokenCount += 1
            }
            if (baseCells.isDead(x, y) && generatedCells.isAlive(x, y) && (count == 2 || count == 3)) {
               brokenCount += 1
            }
            if (baseCells.isAlive(x, y) && generatedCells.isDead(x, y) && count != 3) {
               brokenCount += 1
            }
         }
      }
      brokenCount
   }
}
