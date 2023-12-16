package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 extends Challenge {
  public Result challenge() {
    char[][] grid = getInput().stream()
        .map(String::toCharArray)
        .toArray(char[][]::new);

    long part1 = getLitTiles(grid, new Beam(0, 0, Dir.RIGHT));

    long part2 = Stream.concat(
            IntStream.range(0, grid.length)
                .boxed()
                .flatMap(i -> Stream.of(new Beam(0, i, Dir.RIGHT), new Beam(grid[0].length - 1, i, Dir.LEFT))),
            IntStream.range(0, grid[0].length)
                .boxed()
                .flatMap(i -> Stream.of(new Beam(i, 0, Dir.DOWN), new Beam(i, grid.length - 1, Dir.UP))))
        .mapToLong(beam -> getLitTiles(grid, beam))
        .max().orElse(-1);

    return new Result(part1, part2);
  }

  private long getLitTiles(char[][] grid, Beam start) {
    int[][] light = new int[grid.length][grid[0].length];

    ArrayDeque<Beam> beams = new ArrayDeque<>();
    beams.add(start);

    while (!beams.isEmpty()) {
      Beam beam = beams.removeFirst();
      if (beam.x < 0 || beam.x >= grid[0].length || beam.y < 0 || beam.y >= grid.length) {
        continue;
      }

      char gridElement = grid[beam.y][beam.x];
      int bit = beam.dir.horizontal ? 1 : 2;
      if (Set.of('.', '|', '-').contains(gridElement) && ((light[beam.y][beam.x] & bit) == bit)) {
        continue;
      }
      light[beam.y][beam.x] |= bit;
      beams.addAll(continueBeam(beam, gridElement));
    }

    return Stream.of(light)
        .flatMapToInt(IntStream::of)
        .filter(i -> i > 0)
        .count();
  }

  private List<Beam> continueBeam(Beam beam, char gridElement) {
    final Beam newRight = new Beam(beam.x + 1, beam.y, Dir.RIGHT);
    final Beam newLeft = new Beam(beam.x - 1, beam.y, Dir.LEFT);
    final Beam newDown = new Beam(beam.x, beam.y + 1, Dir.DOWN);
    final Beam newUp = new Beam(beam.x, beam.y - 1, Dir.UP);
    final Beam newHorizontalDir = new Beam(beam.x + beam.dir.sign, beam.y, beam.dir);
    final Beam newVerticalDir = new Beam(beam.x, beam.y + beam.dir.sign, beam.dir);

    return switch (gridElement) {
      case '.' -> beam.dir.horizontal ? List.of(newHorizontalDir) : List.of(newVerticalDir);
      case '-' -> beam.dir.horizontal ? List.of(newHorizontalDir) : List.of(newLeft, newRight);
      case '|' -> !beam.dir.horizontal ? List.of(newVerticalDir) : List.of(newUp, newDown);
      case '/' -> switch (beam.dir) {
        case UP -> List.of(newRight);
        case DOWN -> List.of(newLeft);
        case LEFT -> List.of(newDown);
        case RIGHT -> List.of(newUp);
      };
      case '\\' -> switch (beam.dir) {
        case UP -> List.of(newLeft);
        case DOWN -> List.of(newRight);
        case LEFT -> List.of(newUp);
        case RIGHT -> List.of(newDown);
      };
      default -> throw new RuntimeException("Unknown grid element: " + gridElement);
    };
  }

  record Beam(int x, int y, Dir dir) {}

  enum Dir {
    UP(false, -1), DOWN(false, +1), LEFT(true, -1), RIGHT(true, +1);

    private final boolean horizontal;
    private final int sign;

    Dir(boolean horizontal, int sign) {
      this.horizontal = horizontal;
      this.sign = sign;
    }
  }
}

