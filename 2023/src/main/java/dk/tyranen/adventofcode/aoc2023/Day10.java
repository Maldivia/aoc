package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 extends Challenge {
  public Result challenge() {
    char[][] pipeSystem = getInput().stream()
        .map(String::toCharArray)
        .toArray(char[][]::new);

    Set<Pos> visited = getVisited(pipeSystem);

    long part1 = visited.size() / 2;

    char[][] pipeSystem2 = new char[pipeSystem.length*2][pipeSystem[0].length*2];
    pipeSystem2[pipeSystem.length*2-1][pipeSystem[0].length*2-1] = ':';
    for (int row = 0; row < pipeSystem.length; row++) {
      for (int col = 0; col < pipeSystem[row].length; col++) {
        char newChar = visited.contains(new Pos(row, col)) ? toBox(pipeSystem[row][col]) : '.';
        pipeSystem2[row*2][col*2] = newChar;
        pipeSystem2[row*2+1][col*2] = getRow(newChar);
        pipeSystem2[row*2][col*2+1] = getCol(newChar);
        pipeSystem2[row*2+1][col*2+1] = ':';
      }
    }

    floodFill(pipeSystem2);
    for (char[] row : pipeSystem2) System.out.println(new String(row).replace(':', ' '));

    long part2 = 0;
    for (char[] chars : pipeSystem2) {
      for (char c : chars) {
        if (c == '.')
          part2++;
      }
    }

    return new Result(part1, part2);
  }

  private static Set<Pos> getVisited(char[][] pipeSystem) {
    Set<Pos> visited = new HashSet<>();
    Pos start = null;
    for (int row = 0; row < pipeSystem.length; row++) {
      for (int col = 0; col < pipeSystem[row].length; col++) {
        if (pipeSystem[row][col] == 'S') {
          start = new Pos(row, col);
          break;
        }
      }
    }

    Pos current = start;
    Direction dir = Direction.DOWN;
    do {
      visited.add(current);
      dir = switch (pipeSystem[current.row()][current.col()]) {
        case 'S' -> dir;
        case '|' -> dir;
        case '-' -> dir;
        case 'J' -> (dir == Direction.DOWN) ? Direction.LEFT : Direction.UP;
        case 'L' -> (dir == Direction.DOWN) ? Direction.RIGHT : Direction.UP;
        case '7' -> (dir == Direction.UP) ? Direction.LEFT : Direction.DOWN;
        case 'F' -> (dir == Direction.UP) ? Direction.RIGHT : Direction.DOWN;
        default -> throw new RuntimeException("Invalid path");
      };
      current  = new Pos(current.row() + dir.offsetRow, current.col() + dir.offsetCol);
    } while (!current.equals(start));
    return visited;
  }

  private char getCol(char c) {
    if (List.of('║','╝','╗','.').contains(c)) return ':';
    if (List.of('═','╚','╔').contains(c)) return '═';
    return c;
  }

  private char getRow(char c) {
    if (List.of('║','╗','╔').contains(c)) return '║';
    if (List.of('═','╝','╚','.').contains(c)) return ':';
    return c;
  }


  private char toBox(char c) {
    return switch (c) {
      case 'S' -> '║';
      case '|' -> '║';
      case '-' -> '═';
      case 'J' -> '╝';
      case 'L' -> '╚';
      case '7' -> '╗';
      case 'F' -> '╔';
      default -> c;
    };
  }

  private void floodFill(char[][] matrix) {
    ArrayDeque<Pos> queue = new ArrayDeque<>();
    queue.add(new Pos(0, 0));

    while (!queue.isEmpty()) {
      Pos pos = queue.pop();
      int row = pos.row;
      int col = pos.col;
      if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[row].length) {
        continue;
      }

      if (matrix[row][col] != '.' && matrix[row][col] != ':') {
        continue;
      }

      matrix[row][col] = ' ';
      queue.add(new Pos(row - 1, col));
      queue.add(new Pos(row + 1, col));
      queue.add(new Pos(row, col - 1));
      queue.add(new Pos(row, col + 1));
    }
  }

  record Pos(int row, int col) {}
  enum Direction { UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    final int offsetRow;
    final int offsetCol;

    Direction(int offsetRow, int offsetCol) {
      this.offsetRow = offsetRow;
      this.offsetCol = offsetCol;
    }
  }
}

