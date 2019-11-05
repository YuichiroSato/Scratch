package guis

import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.{BorderLayout, Dimension, Graphics}

import javax.swing._
import logics.GameOfLife

object Main extends App {

  val gameOfLife = new GameOfLife(30, 30)
  val scrollPane = new JScrollPane()

  val panel = new GameOfLifePanel()
  panel.setGame(gameOfLife)
  val frame = new JFrame("Game of life")
  frame.getContentPane.add(scrollPane, BorderLayout.CENTER)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.setSize(new Dimension(620, 750))
  frame.setLocationRelativeTo(null)
  frame.add(panel)
  frame.setVisible(true)
  frame.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = {
      val x = e.getX
      val y = e.getY
      panel.interact(x, y)
    }
  })
  frame.setLayout(null)

  val toggleButton = new JButton("Toggle")
  toggleButton.addActionListener((_: ActionEvent) => {
    panel.toggleGame()
  })
  toggleButton.setBounds(10, 40, 100, 30)
  frame.add(toggleButton)

  val clearButton = new JButton("Clear")
  clearButton.addActionListener((_: ActionEvent) => {
    panel.clearGame()
  })
  clearButton.setBounds(120, 40, 100, 30)
  frame.add(clearButton)
}

class GameOfLifePanel extends JPanel {

  var isEvolved = false
  var gameOfLife: GameOfLife = null
  var gameOfLifeBuk: GameOfLife = null
  val cellSize = 20
  val headerSize = 100
  val titleBarSize = 35

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    g.setColor(java.awt.Color.BLACK)
    if (this.isEvolved) {
      g.drawString("evolved", 5, 15)
    } else {
      g.drawString("draw", 5, 15)
    }

    val cells = if (this.isEvolved) {
      gameOfLife.getCells
    } else {
      gameOfLifeBuk.getCells
    }
    for (x <- 0 until gameOfLife.xSize) {
      for (y <- 0 until gameOfLife.ySize) {
        if (cells(x)(y) == GameOfLife.ALIVE) {
          g.fillRect(x * cellSize, headerSize + y * cellSize, cellSize, cellSize)
        } else {
          g.drawRect(x * cellSize, headerSize + y * cellSize, cellSize, cellSize)
        }
      }
    }
  }

  def setGame(gameOfLife: GameOfLife): Unit = {
    this.gameOfLifeBuk = gameOfLife.copy()
    this.gameOfLife = gameOfLife
    this.gameOfLife.evolve()
    this.isEvolved = false
    repaint()
  }

  def interact(x: Int, y: Int): Unit = {
    if (!isInHeader(x, y)) {
      toggleLife(x, y)
    }
    repaint()
  }

  def toggleGame(): Unit = {
    this.isEvolved = !this.isEvolved
    repaint()
  }

  def clearGame(): Unit = {
    this.gameOfLife.clear()
    this.gameOfLifeBuk.clear()
    this.isEvolved = false
    repaint()
  }

  private def toggleLife(x: Int, y: Int): Unit = {
    if (!this.isEvolved) {
      val xc = x / cellSize
      val yc = (y - headerSize - titleBarSize) / cellSize
      this.gameOfLifeBuk.toggleAlive(xc, yc)
      this.gameOfLife = gameOfLifeBuk.copy()
      this.gameOfLife.evolve()
    }
  }

  private def isInHeader(x: Int, y: Int): Boolean = {
    y < headerSize + titleBarSize
  }
}