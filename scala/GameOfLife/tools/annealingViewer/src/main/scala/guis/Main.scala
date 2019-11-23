package guis

import java.awt.event.{ActionEvent, MouseAdapter, MouseEvent}
import java.awt.{BorderLayout, Dimension, Graphics}

import javax.swing._
import logics.{Annealing, Annealing1, Cells, GameOfLife}

object Main extends App {

  val annealing = if (args.length == 1) {
    args(0) match {
      case "1" => new Annealing1()
    }
  } else {
    new Annealing1()
  }

  val gameOfLife = new GameOfLife(30, 30)
  val scrollPane = new JScrollPane()

  val panel = new GameOfLifePanel()
  panel.setGame(gameOfLife)
  panel.setAnnealing(annealing)
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

  val genButton = new JButton("Best")
  genButton.addActionListener((_: ActionEvent) => {
    panel.showBestGame()
  })
  genButton.setBounds(120, 40, 100, 30)
  genButton.setVisible(true)
  frame.add(genButton)

  val evolveButton = new JButton("Gen")
  evolveButton.addActionListener((_: ActionEvent) => {
    panel.genGame()
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
}

class GameOfLifePanel extends JPanel {

  var state = GameOfLifePanel.DrawState
  var drawnGame: GameOfLife = null
  var bestGame: GameOfLife = null
  var generatedGame: GameOfLife = null
  var annealing: Annealing = null
  val cellSize = 20
  val headerSize = 100
  val titleBarSize = 35

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    g.setColor(java.awt.Color.BLACK)
    this.state match {
      case GameOfLifePanel.DrawState => g.drawString(s"draw ${this.annealing.name}", 5, 15)
      case GameOfLifePanel.BestState => g.drawString(s"best ${this.annealing.name}", 5, 15)
      case GameOfLifePanel.GenState => g.drawString(s"gen ${this.annealing.name}", 5, 15)
    }

    val cells = this.state match {
      case GameOfLifePanel.DrawState => drawnGame.getCells.getCells
      case GameOfLifePanel.BestState => bestGame.getCells.getCells
      case GameOfLifePanel.GenState => generatedGame.getCells.getCells
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
    this.bestGame = gameOfLife.copy()
    this.generatedGame = gameOfLife.copy()
    repaint()
  }

  def setAnnealing(annealing: Annealing): Unit = {
    this.annealing = annealing
  }

  def drawGame(): Unit = {
    this.state = GameOfLifePanel.DrawState
    repaint()
  }

  def showBestGame(): Unit = {
    this.state = GameOfLifePanel.BestState
    repaint()
  }

  def genGame(): Unit = {
    val candidate = this.annealing.generate(this.drawnGame.getCells, this.bestGame.getCells)
    val bestBroken = this.annealing.countBroken(this.drawnGame.getCells, this.bestGame.getCells)
    val candidateBroken = this.annealing.countBroken(this.drawnGame.getCells, candidate)
    println(s"best: $bestBroken candidate: $candidateBroken")
    if (candidateBroken < bestBroken) {
      this.bestGame.setCells(candidate)
      println("Updated!")
    }
    this.generatedGame.setCells(candidate)
    this.state = GameOfLifePanel.GenState
    repaint()
  }

  def clearGame(): Unit = {
    this.drawnGame.clear()
    this.bestGame.clear()
    this.generatedGame.clear()
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
  val BestState = "best"
  val GenState = "gen"
}