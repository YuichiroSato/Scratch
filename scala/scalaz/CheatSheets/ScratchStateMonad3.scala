package monads

import java.awt.{Graphics, Color}
import javax.swing.{JPanel, JFrame}

import scalaz._
import Scalaz._

/**
 * Created by Yuichiro on 2015/01/24.
 */
object ScratchStateMonad3 {

  def main(args: Array[String]): Unit = {
    GameOfLifeTest.run()

    new Thread(new GameOfLife(100, 100).init()).start()
  }

}

case class World(cells: Map[(Int, Int),  Cell])
object World {

  def random: World = {
    val builder = Map.newBuilder[(Int, Int), Cell]
    for (x <- 0 to 10) {
      for (y <- 0 to 10) {
        builder += (x,y) -> Cell.random
      }
    }
    World(builder.result())
  }
}

case class Cell(alive: Boolean)
object Cell {

  def alive: Cell = Cell(alive = true)
  def dead: Cell = Cell(alive = false)
  def random: Cell = if (Math.random() < 0.2) Cell.alive else Cell.dead
}

case class GameOfLife(sx: Int, sy: Int) extends JFrame with Runnable {

  val field = new Field

  def init(): GameOfLife = {
    this.setBounds(0, 0, 400, 400)
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    this.setVisible(true)
    this.show()
    this
  }

  override def run(): Unit = {
    var world = World.random
    var history = List.empty[World]
    while(true) {
      history ::= world
      if (history.size % 2 == 0)
        world = GameOfLife.update(world)
      else
        world = GameOfLife.updateS(world)

      render(world)
      if (oscillate(history) || tooLong(history)) {
        world = World.random
        history = List.empty[World]
      }
      Thread.sleep(100)
    }
  }

  def oscillate(history: List[World]): Boolean = {
    for (i <- 2 to history.size / 2) {
      if (history.take(i) == history.drop(i).take(i)) return true
    }
    false
  }

  def tooLong(history: List[World]): Boolean = 300 < history.size

  def render(world: World): Unit = {
    val bufferStrategy = this.getBufferStrategy()
    if (bufferStrategy == null) {
      this.createBufferStrategy(3)
      return
    }

    val buf = bufferStrategy.getDrawGraphics()
    val x = this.getX
    val y = this.getY
    val w = this.getWidth
    val h = this.getHeight
    buf.setColor(Color.BLACK)
    buf.fillRect(x, y, w, h)

    val l = if (w < h) w else h
    val dl = (l * 0.1).toInt
    field.setBounds(x + dl, y + dl, l - 2 * dl, l - 2 * dl)
    field.show(world, buf)
    buf.dispose()
    bufferStrategy.show()
  }

  class Field extends JPanel {

    def show(world: World, g: Graphics): Unit = {
      g.setColor(Color.GREEN)
      val dl = (this.getWidth / Math.sqrt(world.cells.size)).toInt
      world.cells foreach { xy_cell =>
        val x = this.getX + dl * xy_cell._1._1
        val y = this.getY + dl * xy_cell._1._2
        if (xy_cell._2.alive) {
          g.fillRect(x, y, dl, dl)
        } else {
          g.drawRect(x, y, dl, dl)
        }
      }
    }
  }
}

object GameOfLife {

  def update(world: World): World = {
    val builder = Map.newBuilder[(Int, Int), Cell]
    world.cells foreach { cell =>
      builder += cell._1 -> next(cell._2, cell._1, world)
    }
    World(builder.result())
  }

  def updateS(world: World): World = {
    update.exec(world)
  }

  def update(): State[World, Unit] = for {
    world <- State.init[World]
    keys <- getKeys()
    cells <- getCells()
    counts <- getCount()
    _ <- updateWorld(cells zip counts zip keys)
  } yield ()

  def around(xy: (Int, Int), world: World): List[Cell] = {
    val keys = for (i <- xy._1 - 1 to xy._1 + 1; j <- xy._2 - 1 to xy._2 + 1) yield (i, j)
    keys.toList filter(_ != xy) map (key => world.cells.get(key)) collect { case Some(cell) => cell }
  }

  def countAlive(cells: List[Cell]): Int = cells count (_.alive)

  def next(cell: Cell, xy: (Int, Int), world: World): Cell = {
    val aroundCells = around(xy, world)
    val count = countAlive(aroundCells)
    cellUpdate(cell)(count)
  }

  val cellUpdate: Cell => Int => Cell = (cell: Cell) => (count: Int) => {
    if (cell.alive && (count == 2 || count == 3))
      Cell.alive
    else if (!cell.alive && count == 3)
      Cell.alive
    else
      Cell.dead
  }

  def getKeys() = State[World, List[(Int, Int)]] {
    world => (world, world.cells.toList map (_._1))
  }

  def getCells() = State[World, List[Cell]] {
    world => (world, world.cells.toList map (_._2))
  }

  def getCount() = State[World, List[Int]] {
    world => (world, world.cells.toList map (t => countAlive(around(t._1, world))))
  }

  def updateWorld(input: List[((Cell, Int), (Int, Int))]) = State[World, Unit] {
    world => (World(input map (t => t._2 -> cellUpdate(t._1._1)(t._1._2)) toMap), Unit)
  }
}

object GameOfLifeTest {

  val world = World(Map(
    (0,0) -> Cell.alive, (0,1) -> Cell.alive, (0,2) -> Cell.dead, (0,3) -> Cell.alive, (0,4) -> Cell.dead,
    (1,0) -> Cell.alive, (1,1) -> Cell.dead, (1,2) -> Cell.dead, (1,3) -> Cell.dead, (1,4) -> Cell.dead,
    (2,0) -> Cell.dead, (2,1) -> Cell.dead, (2,2) -> Cell.dead, (2,3) -> Cell.dead, (2,4) -> Cell.alive,
    (3,0) -> Cell.dead, (3,1) -> Cell.dead, (3,2) -> Cell.dead, (3,3) -> Cell.dead, (3,4) -> Cell.dead,
    (4,0) -> Cell.alive, (4,1) -> Cell.alive, (4,2) -> Cell.dead, (4,3) -> Cell.alive, (4,4) -> Cell.dead))

  def run() = {
    testAround()
    testCountAlive()
    testNext()
  }

  def testAround() = {
    val expected1 = List(Cell.alive, Cell.alive, Cell.dead)
    val result1 = GameOfLife.around((0,0), world)
    assert(expected1 == result1)

    val expected2 = List(Cell.dead, Cell.dead, Cell.dead, Cell.dead, Cell.dead)
    val result2 = GameOfLife.around((2,4), world)
    assert(expected2 == result2)

    val expected3 = List(Cell.dead, Cell.dead, Cell.alive, Cell.dead, Cell.dead, Cell.dead, Cell.alive, Cell.dead)
    val result3 = GameOfLife.around((3,3), world)
    assert(expected3 == result3)
  }

  def testCountAlive() = {
    val cells = List(Cell(true), Cell(true), Cell(false), Cell(false), Cell(true))
    assert(3 == GameOfLife.countAlive(cells))
  }

  def testNext() = {
    val expected1 = Cell.alive
    val result1 = GameOfLife.next(world.cells.get(1,1).get, (1,1), world)
    assert(expected1 == result1)

    val expected2 = Cell.dead
    val result2 = GameOfLife.next(world.cells.get(0,2).get, (0,2), world)
    assert(expected2 == result2)

    val expected3 = Cell.dead
    val result3 = GameOfLife.next(world.cells.get(2,4).get, (2,4), world)
    assert(expected3 == result3)

    val expected4 = Cell.alive
    val result4 = GameOfLife.next(world.cells.get(0,0).get, (0,0), world)
    assert(expected4 == result4)
  }
}
