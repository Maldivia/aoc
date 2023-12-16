package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day14 extends Challenge {
  char[][] rockMap;
  public Result challenge() {
    rockMap = getInput().stream().map(String::toCharArray).toArray(char[][]::new);

    tiltUpOrLeft(rockMap, Direction.NORTH);
    long part1 = IntStream.range(0, rockMap.length)
        .mapToLong(i -> countRocks(rockMap[i]) * (rockMap.length - i))
        .sum();

    //printRockMap(rockMap);

    rockMap = getInput().stream().map(String::toCharArray).toArray(char[][]::new);
    Map<String, Integer> cache = new HashMap<>();

    for (int i = 0; i < 1_000_000_000; i++) {
      Integer cached = cache.put(toCacheKey(rockMap), i);
      if (cached != null && i < (1_000_000_000-100)) {
        // cyclic, skip over until near end
        i = ((1_000_000_000 - i) / (i - cached)) * (i - cached) + i;
      }

      tiltUpOrLeft(rockMap, Direction.NORTH);
      tiltUpOrLeft(rockMap, Direction.WEST);
      tiltDownOrRight(rockMap, Direction.SOUTH);
      tiltDownOrRight(rockMap, Direction.EAST);
    }

    long part2 = IntStream.range(0, rockMap.length)
        .mapToLong(i -> countRocks(rockMap[i]) * (rockMap.length - i))
        .sum();

    return new Result(part1, part2);
  }

  private void printRockMap(char[][] rockMap) {
    Stream.of(rockMap).map(String::new).forEach(System.out::println);
    System.out.println();
  }

  private String toCacheKey(char[][] rockMap) {
    return Stream.of(rockMap).map(String::new).collect(Collectors.joining());
  }

  private void tiltUpOrLeft(char[][] rockMap, Direction dir) {
    for (int row = 0; row < rockMap.length; row++) {
      char[] line = rockMap[row];
      for (int col = 0; col < line.length; col++) {
        char c = line[col];
        if (c == 'O') {
          move(rockMap, row, col, dir);
        }
      }
    }
  }

  private void tiltDownOrRight(char[][] rockMap, Direction dir) {
    for (int row = rockMap.length -1; row >= 0; row--) {
      char[] line = rockMap[row];
      for (int col = line.length - 1; col >= 0; col--) {
        char c = line[col];
        if (c == 'O') {
          move(rockMap, row, col, dir);
        }
      }
    }
  }

  private void move(char[][] rockMap, int row, int col, Direction dir) {
    if (!dir.canMove(col, row, rockMap[row].length, rockMap.length)) return;
    if (rockMap[dir.adjustRow(row)][dir.adjustCol(col)] != '.') return;
    rockMap[dir.adjustRow(row)][dir.adjustCol(col)] = 'O';
    rockMap[row][col] = '.';
    move(rockMap, dir.adjustRow(row), dir.adjustCol(col), dir);
  }

  private long countRocks(char[] chars) {
    long count = 0;
    for (char aChar : chars) {
      if (aChar == 'O') count++;
    }
    return count;
  }

  enum Direction {
    NORTH(col -> col, row -> row - 1, (col, row, maxCol, maxRow) -> row > 0),
    EAST(col -> col + 1, row -> row, (col, row, maxCol, maxRow) -> col < maxCol - 1),
    SOUTH(col -> col, row -> row + 1, (col, row, maxCol, maxRow) -> row < maxRow - 1),
    WEST(col -> col - 1, row -> row, (col, row, maxCol, maxRow) -> col > 0),
    ;
    private final IntUnaryOperator adjustCol;
    private final IntUnaryOperator adjustRow;
    private final MovePredicate movePredicate;

    interface MovePredicate {
      boolean canMove(int col, int row, int maxCol, int maxRow);
    }
    private Direction(IntUnaryOperator adjustCol, IntUnaryOperator adjustRow, MovePredicate movePredicate) {
      this.adjustCol = adjustCol;
      this.adjustRow = adjustRow;
      this.movePredicate = movePredicate;
    }

    public int adjustCol(int col) {
      return adjustCol.applyAsInt(col);
    }

    public int adjustRow(int row) {
      return adjustRow.applyAsInt(row);
    }

    public boolean canMove(int col, int row, int maxCol, int maxRow) {
      return movePredicate.canMove(col, row, maxCol, maxRow);
    }
  }
}

