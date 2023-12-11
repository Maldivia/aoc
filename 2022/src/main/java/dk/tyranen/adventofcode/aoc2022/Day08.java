package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day08 extends Challenge {
  public Result challenge() {
    int[][] matrix = getInput().stream()
        .map(s -> s.chars().map(c -> c - '0').toArray())
        .toArray(int[][]::new);

    long part1 = IntStream.range(0, matrix.length)
        .mapToLong(i -> IntStream.range(0, matrix[i].length)
            .filter(j -> isVisible(matrix, i, j))
            .count())
        .sum();

    long part2 = IntStream.range(0, matrix.length)
        .mapToLong(i -> IntStream.range(0, matrix[i].length)
            .mapToLong(j -> findViewArea(matrix, i, j))
            .max().orElse(-1))
        .max().orElse(-1);

    return new Result(part1, part2);
  }

  private boolean isVisible(int[][] matrix, int i, int j) {
    int height = matrix[i][j];
    return
        IntStream.iterate(i - 1, row -> row >= 0, row -> row - 1).allMatch(row -> matrix[row][j] < height)
            || IntStream.range(i + 1, matrix.length).allMatch(row -> matrix[row][j] < height)
            || IntStream.iterate(j - 1, col -> col >= 0, col -> col - 1).allMatch(col -> matrix[i][col] < height)
            || IntStream.range(j + 1, matrix[i].length).allMatch(col -> matrix[i][col] < height);
  }

  private long findViewArea(int[][] matrix, int i, int j) {
    int height = matrix[i][j];

    int left = j - Math.max(0, IntStream.iterate(j - 1, col -> col >= 0, col -> col - 1).takeWhile(col -> matrix[i][col] < height).min().orElse(j) - 1);
    int right = Math.min(matrix[i].length - 1, IntStream.range(j + 1, matrix[i].length).takeWhile(col -> matrix[i][col] < height).max().orElse(j) + 1) - j;
    int up = i - Math.max(0, IntStream.iterate(i - 1, row -> row >= 0, row -> row - 1).takeWhile(row -> matrix[row][j] < height).min().orElse(i) - 1);
    int down = Math.min(matrix.length - 1, IntStream.range(i + 1, matrix.length).takeWhile(row -> matrix[row][j] < height).max().orElse(i) + 1) - i;

    return (long) left * right * up * down;
  }
}