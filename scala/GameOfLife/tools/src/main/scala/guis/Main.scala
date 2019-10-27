package guis

import java.awt.{BorderLayout, Dimension, Graphics}

import javax.swing._
import logics.GameOfLife

object Main extends App {

  val gameOfLife = new GameOfLife(100, 100)
  val scrollPane = new JScrollPane()

  val panel = new GameOfLifePanel()
  gameOfLife.randomize(0.4)
  panel.setGame(gameOfLife)
  val frame = new JFrame("Game of life")
  frame.getContentPane.add(scrollPane, BorderLayout.CENTER)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.setSize(new Dimension(600, 700))
  frame.setLocationRelativeTo(null)
  frame.add(panel)
  frame.setVisible(true)

  while (true) {
    gameOfLife.evolve()
    panel.setGame(gameOfLife)
    Thread.sleep(1000)
  }
}

class GameOfLifePanel extends JPanel {

  var gameOfLife: GameOfLife = null
  val cellSize = 6

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    g.setColor(java.awt.Color.BLACK)
    val cells = gameOfLife.getCells
    for (x <- 0 until gameOfLife.xSize) {
      for (y <- 0 until gameOfLife.ySize) {
        if (cells(x)(y) == GameOfLife.ALIVE) {
          g.fillRect(x * cellSize, 100 + y * cellSize, cellSize, cellSize)
        } else {
          g.drawRect(x * cellSize, 100 + y * cellSize, cellSize, cellSize)
        }
      }
    }
  }

  def setGame(gameOfLife: GameOfLife): Unit = {
    this.gameOfLife = gameOfLife
    repaint()
  }
}