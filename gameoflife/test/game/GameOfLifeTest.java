package game;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static game.GameOfLife.CellState.*;

class GameOfLifeTest {
  @Test
  void canary() {
    assert (true);
  }

  @Test
  void deadCellBehaviors() {
    assertAll(
      () -> assertEquals(DEAD, GameOfLife.nextCellState(DEAD, 0)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(DEAD, 1)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(DEAD, 2)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(DEAD, 5)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(DEAD, 8)),
      () -> assertEquals(ALIVE, GameOfLife.nextCellState(DEAD, 3))
    );
  }

  @Test
  void aliveCellBehaviors() {
    assertAll(
      () -> assertEquals(DEAD, GameOfLife.nextCellState(ALIVE, 1)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(ALIVE, 4)),
      () -> assertEquals(DEAD, GameOfLife.nextCellState(ALIVE, 8)),
      () -> assertEquals(ALIVE, GameOfLife.nextCellState(ALIVE, 2)),
      () -> assertEquals(ALIVE, GameOfLife.nextCellState(ALIVE, 3))
    );
  }

  @Test
  void generateSignalsForPosition3_3() {
    assertEquals(List.of(new Point(2, 2), new Point(2, 3), new Point(2, 4),
      new Point(3, 2), new Point(3, 4), new Point(4, 2), new Point(4, 3), new Point(4, 4)),
      GameOfLife.generateSignals(new Point(3, 3)));
  }

  @Test
  void generateSignalsForPosition2_4() {
    assertEquals(List.of(new Point(1, 3), new Point(1, 4), new Point(1, 5),
      new Point(2, 3), new Point(2, 5), new Point(3, 3), new Point(3, 4), new Point(3, 5)),
      GameOfLife.generateSignals(new Point(2, 4)));
  }

  @Test
  void generateSignalsForPosition0_0() {
    assertEquals(List.of(new Point(-1, -1), new Point(-1, 0), new Point(-1, 1),
      new Point(0, -1), new Point(0, 1), new Point(1, -1), new Point(1, 0), new Point(1, 1)),
      GameOfLife.generateSignals(new Point(0, 0)));
  }

  @Test
  void generateSignalsForPositions0Generates0() {
    assertEquals(0, GameOfLife.generateSignalsForPositions(Collections.emptyList()).size());
  }

  @Test
  void generateSignalsForPositions1Generates8() {
    assertEquals(8, GameOfLife.generateSignalsForPositions(
      List.of(new Point())
    ).size());
  }

  @Test
  void generateSignalsForPositions2Generates16() {
    assertEquals(16, GameOfLife.generateSignalsForPositions(List.of(
      new Point(), new Point()
    )).size());
  }

  @Test
  void generateSignalsForPositions3Generates24() {
    assertEquals(24, GameOfLife.generateSignalsForPositions(List.of(
      new Point(), new Point(), new Point()
    )).size());
  }

  @Test
  void countSignals0PointsEmptyMap() {
    assertEquals(0, GameOfLife.countSignals(Collections.emptyList()).size());
  }

  @Test
  void countSignals1PointTo1() {
    Point key = new Point();
    Map<Point, Integer> signals = GameOfLife.countSignals(List.of(key));
    assertEquals(1, signals.size());
    assertEquals((Integer) 1, signals.getOrDefault(key, 0));
  }

  @Test
  void countSignals2SamePointsTo2() {
    Point key = new Point();
    Map<Point, Integer> signals = GameOfLife.countSignals(List.of(key, key));
    assertEquals(1, signals.size());
    assertEquals((Integer) 2, signals.getOrDefault(key, 0));
  }

  @Test
  void countSignals3PointsWith2SameMapsTo2Keys() {
    Point key1 = new Point(0, 0);
    Point key2 = new Point(1, 1);

    Map<Point, Integer> signals = GameOfLife.countSignals(List.of(key1, key2, key1));
    assertEquals(2, signals.size());
    assertEquals((Integer) 2, signals.getOrDefault(key1, 0));
    assertEquals((Integer) 1, signals.getOrDefault(key2, 0));
  }

  @Test
  void nextGenerationBehaviorForEmptyWorld() {
    assertEquals(0, GameOfLife.nextGeneration(Collections.emptyList()).size());
  }

  @Test
  void nextGenerationBehaviorForOneCell() {
    assertEquals(0, GameOfLife.nextGeneration(List.of(new Point())).size());
  }

  @Test
  void nextGenerationBehaviorFor_2_3_and_2_4() {
    assertEquals(0, GameOfLife.nextGeneration(List.of(
      new Point(2, 3),
      new Point(2, 4)
    )).size());
  }

  @Test
  void nextGenerationBehaviorFor_1_1_and_1_2_and_3_0() {
    assertEquals(List.of(new Point(2, 1)), GameOfLife.nextGeneration(List.of(
      new Point(1, 1),
      new Point(1, 2),
      new Point(3, 0)
    )));
  }

  @Test
  void nextGenerationBehaviorFor_1_1_and_1_2_and_2_2() {
    assertEquals(
      new HashSet<Point>(
        List.of(
          new Point(1, 1),
          new Point(1, 2),
          new Point(2, 2),
          new Point(2, 1)
        )),
      new HashSet<Point>(
        GameOfLife.nextGeneration(List.of(
          new Point(1, 1),
          new Point(1, 2),
          new Point(2, 2)
        )))
    );
  }

  @Test
  void blockToBlock21_31_20_23() {
    assertEquals(
      new HashSet<Point>(
        List.of(
          new Point(2, 1),
          new Point(2, 0),
          new Point(3, 1),
          new Point(3, 0)
        )),
      new HashSet<Point>(GameOfLife.nextGeneration(List.of(
        new Point(2, 1),
        new Point(3, 1),
        new Point(2, 0),
        new Point(3, 0)
      )))
    );
  }

  @Test
  void beeHiveToBeeHive() {
    assertEquals(
      new HashSet<Point>(
        List.of(
          new Point(6, 3),
          new Point(4, 2),
          new Point(5, 1),
          new Point(6, 1),
          new Point(5, 3),
          new Point(7, 2)
        )),
      new HashSet<Point>(GameOfLife.nextGeneration(List.of(
        new Point(5, 3),
        new Point(6, 3),
        new Point(7, 2),
        new Point(6, 1),
        new Point(5, 1),
        new Point(4, 2)
      )))
    );
  }

  @Test
  void horizontalBlinkerToVerticalBlinker() {
    assertEquals(
      new HashSet<Point>(List.of(
        new Point(4, 4),
        new Point(4, 5),
        new Point(4, 3)
      )),
      new HashSet<Point>(GameOfLife.nextGeneration(List.of(
        new Point(3, 4),
        new Point(4, 4),
        new Point(5, 4)
      )))
    );
  }

  @Test
  void verticalBlinkerToHorizontalBlinker() {
    assertEquals(
      List.of(
        new Point(8, 8),
        new Point(9, 8),
        new Point(7, 8)
      ),
      GameOfLife.nextGeneration(List.of(
        new Point(8, 9),
        new Point(8, 8),
        new Point(8, 7)
      ))
    );
  }

  @Test
  void gliderTransition() {
    assertEquals(
      new HashSet<Point>(
        List.of(
          new Point(6, 3),
          new Point(4, 4),
          new Point(5, 2),
          new Point(6, 4),
          new Point(5, 3)
        )),
      new HashSet<Point>(GameOfLife.nextGeneration(List.of(
        new Point(5, 5),
        new Point(6, 4),
        new Point(6, 3),
        new Point(5, 3),
        new Point(4, 3)
      )))
    );
  }

  @Test
  void getBoundsEmptyListBehavior() {
    assertTrue(GameOfLife.getBounds(new ArrayList<Point>()).isEmpty());
  }

  @Test
  void getBoundsOnePointDuplicateBehavior() {
    Point testPoint = new Point();
    assertEquals(
      List.of(testPoint, testPoint),
      GameOfLife.getBounds(List.of(testPoint))
    );
  }

  @Test
  void getBoundsTwoPointsBehavior() {
    Point topLeft = new Point(1, 1);
    Point bottomRight = new Point(2, 2);
    assertEquals(
      List.of(topLeft, bottomRight),
      GameOfLife.getBounds(List.of(bottomRight, topLeft))
    );
  }

  @Test
  void getBoundsThreePointsBehavior() {
    Point point1 = new Point(0, 0);
    Point point2 = new Point(2, 3);
    Point point3 = new Point(3, 2);
    assertEquals(
      List.of(point1, new Point(3, 3)),
      GameOfLife.getBounds(List.of(point1, point2, point3))
    );
  }

  @Test
  void getBoundsFourPointsBehavior() {
    Point point1 = new Point(1, 5);
    Point point2 = new Point(3, 3);
    Point point3 = new Point(2, 4);
    Point point4 = new Point(7, 1);
    assertEquals(
      new HashSet<Point>(List.of(new Point(1, 1), new Point(7, 5))),
      new HashSet<Point>(GameOfLife.getBounds(List.of(point1, point2, point3, point4)))
    );
  }

  @Test
  void parsePointsEmptyTest() {
    assertEquals(
      Collections.emptyList(),
      GameOfLife.parsePoints("")
    );
  }

  @Test
  void parsePointsInvalidStringBehavior() {
    assertAll(
      () -> assertEquals(Collections.emptyList(), GameOfLife.parsePoints("")),
      () -> assertEquals(Collections.emptyList(), GameOfLife.parsePoints("rewgkewgw")),
      () -> assertEquals(Collections.emptyList(), GameOfLife.parsePoints("1)")),
      () -> assertEquals(Collections.emptyList(), GameOfLife.parsePoints("1(2)")),
      () -> assertEquals(Collections.emptyList(), GameOfLife.parsePoints("(1,2"))
    );
  }

  @Test
  void parsePointsOnePointTest() {
    List<Point> expected = List.of(new Point(1, 2));
    assertAll(
      () -> assertEquals(expected, GameOfLife.parsePoints("(1,2)")),
      () -> assertEquals(expected, GameOfLife.parsePoints("   (1,2)")),
      () -> assertEquals(expected, GameOfLife.parsePoints("(  1,2)")),
      () -> assertEquals(expected, GameOfLife.parsePoints("(  1,2)  ")),
      () -> assertEquals(expected, GameOfLife.parsePoints("(  1,  2)  ")),
      () -> assertEquals(expected, GameOfLife.parsePoints("(  1  ,  2)  ")),
      () -> assertEquals(expected, GameOfLife.parsePoints("(  1  ,  2 )  "))
    );
  }

  @Test
  void parsePointsTwoPointTest() {
    assertEquals(
      List.of(new Point(1, 2), new Point(3, 4)),
      GameOfLife.parsePoints("(1, 2)(3, 4)")
    );
  }

  @Test
  void parsePointsTwoSamePointTest() {
    assertEquals(
      List.of(
        new Point(1, 2)
      ),
      GameOfLife.parsePoints("(1, 2) (1, 2)")
    );
  }
}

