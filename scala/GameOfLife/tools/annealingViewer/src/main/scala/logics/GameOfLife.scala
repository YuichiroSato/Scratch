package logics

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
