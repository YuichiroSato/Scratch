package logics

import org.scalatest.{FlatSpec, Matchers}

class ScatteringSpec  extends FlatSpec with Matchers {

  "Scattering" should "generate random scattering pattern" in {
    val cells = new Cells(5, 5)
    val scattering = new Scattering()
    scattering.randomize(3)
    cells.allocate(scattering, 1, 1)
    cells.countAlive(1, 1) should be (3)
  }
}
