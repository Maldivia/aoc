package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class Day06 extends Challenge {
  public Result challenge() {
    List<Character> input = getInput().getFirst().chars().mapToObj(c -> (char) c).toList();

    int part1 = findIndex(input, 4);
    int part2 = findIndex(input, 14);
    return new Result(part1, part2);
  }

  private int findIndex(List<Character> input, int size) {
    return IntStream.range(size, input.size())
        .filter(i -> new HashSet<>(input.subList(i - size, i)).size() == size)
        .findFirst().orElse(-2);
  }
}

