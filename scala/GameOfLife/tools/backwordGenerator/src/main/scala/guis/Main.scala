package guis

import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.{BorderLayout, Dimension, Graphics}

import javax.swing._
import logics.{Cells, GameOfLife}

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

  val drawButton = new JButton("Draw")
  drawButton.addActionListener((_: ActionEvent) => {
    panel.drawGame()
  })
  drawButton.setBounds(10, 40, 100, 30)
  drawButton.setVisible(true)
  frame.add(drawButton)

  val genButton = new JButton("Gen")
  genButton.addActionListener((_: ActionEvent) => {
    panel.genGame()
  })
  genButton.setBounds(120, 40, 100, 30)
  genButton.setVisible(true)
  frame.add(genButton)

  val evolveButton = new JButton("Evolve")
  evolveButton.addActionListener((_: ActionEvent) => {
    panel.evolveGame()
  })
  evolveButton.setBounds(230, 40, 100, 30)
  evolveButton.setVisible(true)
  frame.add(evolveButton)

  val clearButton = new JButton("Clear")
  clearButton.addActionListener((_: ActionEvent) => {
    panel.clearGame()
  })
  clearButton.setBounds(340, 40, 100, 30)
  clearButton.setVisible(true)
  frame.add(clearButton)

  val copyButton = new JButton("Copy")
  copyButton.addActionListener((_: ActionEvent) => {
    panel.copyGame()
  })
  copyButton.setBounds(450, 40, 100, 30)
  copyButton.setVisible(true)
  frame.add(copyButton)
}

class GameOfLifePanel extends JPanel {

  var state = GameOfLifePanel.DrawState
  var drawnGame: GameOfLife = null
  var generatedGame: GameOfLife = null
  var evolvedGame: GameOfLife = null
  val cellSize = 20
  val headerSize = 100
  val titleBarSize = 35

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    g.setColor(java.awt.Color.BLACK)
    this.state match {
      case GameOfLifePanel.DrawState => g.drawString("draw", 5, 15)
      case GameOfLifePanel.GenState => g.drawString("gen", 5, 15)
      case GameOfLifePanel.EvolvedState => g.drawString("evolved", 5, 15)
    }

    val cells = this.state match {
      case GameOfLifePanel.DrawState => drawnGame.getCells.getCells
      case GameOfLifePanel.GenState => generatedGame.getCells.getCells
      case GameOfLifePanel.EvolvedState => evolvedGame.getCells.getCells
    }

    for (x <- 0 until drawnGame.xSize) {
      for (y <- 0 until drawnGame.ySize) {
        if (cells(x)(y) == Cells.ALIVE) {
          g.fillRect(x * cellSize, headerSize + y * cellSize, cellSize, cellSize)
        } else {
          g.drawRect(x * cellSize, headerSize + y * cellSize, cellSize, cellSize)
        }
      }
    }
  }

  def setGame(gameOfLife: GameOfLife): Unit = {
    this.drawnGame = gameOfLife.copy()
    this.generatedGame = gameOfLife.copy()
    this.evolvedGame = gameOfLife.copy()
    repaint()
  }

  def drawGame(): Unit = {
    this.state = GameOfLifePanel.DrawState
    repaint()
  }

  def genGame(): Unit = {
    this.generatedGame.setCells(this.drawnGame.getCells)
    this.generatedGame.backward()
    this.state = GameOfLifePanel.GenState
    repaint()
  }

  def evolveGame(): Unit = {
    this.evolvedGame.setCells(this.generatedGame.getCells)
    this.evolvedGame.evolve()
    this.state = GameOfLifePanel.EvolvedState
    repaint()
  }

  def clearGame(): Unit = {
    this.drawnGame.clear()
    this.generatedGame.clear()
    this.evolvedGame.clear()
    this.state = GameOfLifePanel.DrawState
    repaint()
  }

  def copyGame(): Unit = {
    this.drawnGame.clear()
    this.drawnGame.setCells(this.generatedGame.getCells)
    this.state = GameOfLifePanel.DrawState
    repaint()
  }

  def interact(x: Int, y: Int): Unit = {
    if (!isInHeader(x, y)) {
      toggleLife(x, y)
    }
    repaint()
  }

  private def toggleLife(x: Int, y: Int): Unit = {
    if (this.state == GameOfLifePanel.DrawState) {
      val xc = x / cellSize
      val yc = (y - headerSize - titleBarSize) / cellSize
      this.drawnGame.toggleAlive(xc, yc)
    }
  }

  private def isInHeader(x: Int, y: Int): Boolean = {
    y < headerSize + titleBarSize
  }
}

object GameOfLifePanel {

  val DrawState = "draw"
  val GenState = "gen"
  val EvolvedState = "evolve"
}