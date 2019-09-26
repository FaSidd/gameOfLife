package game;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface GameOfLife {

  public enum CellState {DEAD, ALIVE}

  public static CellState nextCellState(CellState currentState, int numberOfLiveNeighbors) {
    return numberOfLiveNeighbors == 3 || currentState == CellState.ALIVE && numberOfLiveNeighbors == 2 ?
      CellState.ALIVE : CellState.DEAD;
  }

  public static List<Point> generateSignals(Point position) {
    int x = position.x;
    int y = position.y;

    return List.of(new Point(x - 1, y - 1), new Point(x - 1, y), new Point(x - 1, y + 1),
      new Point(x, y - 1), new Point(x, y + 1),
      new Point(x + 1, y - 1), new Point(x + 1, y), new Point(x + 1, y + 1));
  }

  static List<Point> generateSignalsForPositions(List<Point> points) {
    return points.stream()
      .map(GameOfLife::generateSignals)
      .flatMap(List::stream)
      .collect(toList());
  }

  public static Map<Point, Integer> countSignals(List<Point> rawSignals) {
    return rawSignals.stream().collect(toMap(point -> point, value -> 1, Integer::sum));
  }

  public static List<Point> nextGeneration(List<Point> currentGeneration) {
    Map<Point, Integer> signalCounts = countSignals(generateSignalsForPositions(currentGeneration));
    return signalCounts.keySet().stream()
            .filter(cell -> (nextCellState(
              currentGeneration.contains(cell) ? CellState.ALIVE : CellState.DEAD,
              signalCounts.getOrDefault(cell, 0)
            ) == CellState.ALIVE))
            .collect(toList());
  }

  public static List<Point> getBounds(List<Point> cells) {
    if (cells.isEmpty()) {
      return new ArrayList<Point>();
    }

    int maxX, minX, maxY, minY;
    maxX = minX = cells.get(0).x;
    maxY = minY = cells.get(0).y;

    for (Point p : cells) {
      if (p.x < minX) {
        minX = p.x;
      } else if (p.x > maxX) {
        maxX = p.x;
      }
      if (p.y < minY) {
        minY = p.y;
      } else if (p.y > maxY) {
        maxY = p.y;
      }
    }

    Point topLeft = new Point(minX, minY);
    Point bottomRight = new Point(maxX, maxY);
    return List.of(topLeft, bottomRight);
  }

  public static List<Point> parsePoints(String arg) {
    final Pattern POINT_LIST_PATTERN = Pattern.compile("(\\s*\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)*");
    final Pattern POINT_PATTERN = Pattern.compile("(?:\\(\\s*)(\\d+)(?:\\s*,\\s*)(\\d+)(?:\\s*\\))");

    if (!POINT_LIST_PATTERN.matcher(arg).matches()) return Collections.emptyList();

    Matcher matcher = POINT_PATTERN.matcher(arg);
    Set<Point> points = new HashSet<>();
    while (matcher.find()) {
      points.add(new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
    }
    return new ArrayList<>(points);
  }
}
