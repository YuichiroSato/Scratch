package logics

import org.scalatest.{FlatSpec, Matchers}

class ScatteringSpec  extends FlatSpec with Matchers {

  "Scattering" should "generate random scattering pattern" in {
    val cells = new Cells(5, 5)
    val scattering = new Scattering()
    scattering.randomize(3)
    cells.overlay(scattering, 1, 1)
    cells.countAlive(1, 1) should be (3)
  }

  "Scattering" should "kill living cells" in {
    val cells = new Cells(5, 5)
    val scattering = new Scattering()
    scattering.randomize(4)
    scattering.killCells(2)
    cells.overlay(scattering, 1, 1)
    cells.countAlive(1, 1) should be (2)
  }
}
