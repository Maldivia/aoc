package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day09 extends Challenge {
  public Result challenge() {
    long part1 = getInput().stream()
        .map(s -> Stream.of(s.split(" ")).mapToLong(Long::parseLong).toArray())
        .mapToLong(a -> predict(a, false))
        .sum();

    long part2 = getInput().stream()
        .map(s -> Stream.of(s.split(" ")).mapToLong(Long::parseLong).toArray())
        .mapToLong(a -> predict(a, true))
        .sum();
    return new Result(part1, part2);
  }

  private long predict(long[] a, boolean backwards) {
    long[] diffs = diffs(a);
    long add = isSameValue(diffs) ? diffs[0] : predict(diffs, backwards);
    return backwards ? a[0] - add : a[a.length - 1] + add;
  }

  private boolean isSameValue(long[] a) {
    for (long i : a) {
      if (i != a[0]) return false;
    }
    return true;
  }

  private long[] diffs(long[] a) {
    long[] diffs = new long[a.length - 1];
    for (int i = 0; i < a.length - 1; i++) {
      diffs[i] = a[i + 1] - a[i];
    }
    return diffs;
  }
}

