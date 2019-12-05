package logics

import org.scalatest._

class CellsSpec extends FlatSpec with Matchers {

  // Cells are
  //
  // x _ _ _
  // _ _ _ _
  // _ x _ _
  // _ _ _ _
  //
  // x: alive, _: dead
  "Cells" should "count up alive cells" in {
    val cells = new Cells(4, 4)
    cells.setAlive(0, 0)
    cells.setAlive(2, 1)

    cells.countAlive(0, 0) should be (0)
    cells.countAlive(0, 1) should be (1)
    cells.countAlive(0, 2) should be (0)
    cells.countAlive(0, 3) should be (1)

    cells.countAlive(1, 0) should be (2)
    cells.countAlive(1, 1) should be (2)
    cells.countAlive(1, 2) should be (1)
    cells.countAlive(1, 3) should be (1)

    cells.countAlive(2, 0) should be (1)
    cells.countAlive(2, 1) should be (0)
    cells.countAlive(2, 2) should be (1)
    cells.countAlive(2, 3) should be (0)

    cells.countAlive(3, 0) should be (2)
    cells.countAlive(3, 1) should be (2)
    cells.countAlive(3, 2) should be (1)
    cells.countAlive(3, 3) should be (1)
  }
}
