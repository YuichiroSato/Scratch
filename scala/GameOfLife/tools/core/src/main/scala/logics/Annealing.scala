package logics

trait Annealing {

  val name: String

  def generate(baseCells: Cells, bestCells: Cells): Cells

  def countBroken(baseCells: Cells, generatedCells: Cells): Double
}
