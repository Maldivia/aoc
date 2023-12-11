package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 extends Challenge {
  public Result challenge() {

    List<Integer> cycles = new ArrayList<>();
    cycles.add(1);
    for (String s : getInput()) {
      cycles.add(cycles.getLast());
      if (s.startsWith("addx")) {
        cycles.add(Integer.parseInt(s.substring(5)) + cycles.getLast());
      }
    }

    int part1 = IntStream.iterate(20, i -> i < cycles.size(), i -> i + 40)
        .map(i -> cycles.get(i-1) * i)
        .sum();

    System.out.println("Part 2:");
    for (int i = 0; i < 240; i++) {
      int offset = i % 40;
      System.out.print(Math.abs((cycles.get(i) - offset)) <= 1 ? '#' : ' ');
      if (offset == 39) {
        System.out.println();
      }
    }

    return new Result(part1, -1);
  }
}