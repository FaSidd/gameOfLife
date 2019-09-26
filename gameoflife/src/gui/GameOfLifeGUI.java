package gui;

import game.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameOfLifeGUI extends JPanel implements ActionListener {

  public static void main(String[] args) {
    List<Point> startingSeed = GameOfLife.parsePoints(args.length > 0 ? args[0] : "");
    GameOfLifeGUI gameOfLifeGUI = new GameOfLifeGUI(640, 480, startingSeed);

    JFrame frame;
    frame = new JFrame();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setTitle("Game of Life");
    frame.setResizable(false);
    frame.add(gameOfLifeGUI);
    frame.pack();
    frame.setVisible(true);
  }

  private List<Point> generation;
  private Timer frameTimer;

  private GameOfLifeGUI(int W, int H, List<Point> seed) {
    generation = seed.isEmpty() ? List.of(
      new Point(1, 1), new Point(2, 1), new Point(1, 2),
      new Point(4, 4), new Point(4, 3), new Point(3, 4)
      ,
      new Point(6, 5),
      new Point(7, 6),
      new Point(5, 7), new Point(6, 7), new Point(7, 7)
    ) : seed;

    frameTimer = new Timer(500, this); // 3 FPS
    frameTimer.start();

    this.setPreferredSize(new Dimension(W, H));
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == frameTimer) {
      repaint();
      generation = GameOfLife.nextGeneration(generation);
    }
  }

  private void paintBackground(Graphics g) {
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
  }

  private void paintCells(Graphics g, List<Point> cells, Point topLeft, int cellLength) {
    // Paint only live cells
    g.setColor(Color.YELLOW);
    for (Point cell : generation) {
      int startX = (cell.x - topLeft.x + 1) * cellLength;
      int startY = (cell.y - topLeft.y + 1) * cellLength;

      g.fillRect(startX, startY, cellLength, cellLength);
    }
  }

  private void paintGrid(Graphics g, int worldWidth, int worldHeight, int cellLength) {
    if (cellLength <= 3) return; // dont bother

    g.setColor(Color.WHITE);
    for (int row = 0; row < worldHeight + 3; row++) {
      g.drawLine(0, row * cellLength, this.getWidth(), row * cellLength);
    }
    for (int col = 0; col < worldWidth + 3; col++) {
      g.drawLine(col * cellLength, 0, col * cellLength, this.getHeight());
    }
  }

  private void paintGame(Graphics g) {
    if (generation.isEmpty()) return;

    // Calculate Bounds
    List<Point> bounds = GameOfLife.getBounds(generation);
    Point topLeft = bounds.get(0);
    Point bottomRight = bounds.get(1);

    int worldWidth = bottomRight.x - topLeft.x + 1;
    int worldHeight = bottomRight.y - topLeft.y + 1;
    int cellLength = Math.min(this.getWidth() / (worldWidth + 2), this.getHeight() / (worldHeight + 2));
    if (cellLength == 0) { // no point rendering
      frameTimer.stop();
      return;
    }
    this.paintCells(g, generation, topLeft, cellLength);
    this.paintGrid(g, worldWidth, worldWidth, cellLength);
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.paintBackground(g);
    this.paintGame(g);
  }
}
