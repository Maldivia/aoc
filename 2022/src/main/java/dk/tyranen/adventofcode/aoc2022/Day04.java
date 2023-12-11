package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04 extends Challenge {
  @Override
  public Result challenge() {
    long part1 = getInput().stream()
        .map(s -> Stream.of(s.split("[,-]")).mapToInt(Integer::parseInt).toArray())
        .filter(this::fullyOverlaps)
        .count();

    long part2 = getInput().stream()
        .map(s -> Stream.of(s.split("[,-]")).mapToInt(Integer::parseInt).toArray())
        .filter(this::overlaps)
        .count();

    return new Result(part1, part2);
  }

  private boolean fullyOverlaps(int[] ints) {
    return (ints[0] <= ints[2] && ints[1] >= ints[3]) || ints[2] <= ints[0] && ints[3] >= ints[1];
  }

  private boolean overlaps(int[] ints) {
    return !(ints[0] > ints[3] || ints[1] < ints[2]);
  }
}
