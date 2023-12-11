package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 extends Challenge {
  @Override
  public Result challenge() {
    int part1 = getInput().stream()
        .map(this::split)
        .map(this::same)
        .mapToInt(this::toInt)
        .sum();

    int part2 = IntStream.range(0, getInput().size() / 3)
        .mapToObj(i -> List.of(toSet(getInput().get(i*3)), toSet(getInput().get(i*3+1)), toSet(getInput().get(i*3+2))))
        .map(this::same)
        .mapToInt(this::toInt)
        .sum();

    return new Result(part1, part2);
  }

  private int toInt(char c) {
    if (c <= 'Z') return c - 'A' + 27;
    else return c - 'a' + 1;
  }

  private Character same(List<Set<Character>> sets) {
    return sets.get(0).stream().filter(sets.get(1)::contains).filter(c -> sets.size() < 3 ||sets.get(2).contains(c)).findFirst().get();
  }

  List<Set<Character>> split(String line) {
    int size = line.length() / 2;
    return List.of(
        toSet(line.substring(0, size)),
        toSet(line.substring(size)));
  }

  Set<Character> toSet(String s) {
    return s.chars().mapToObj(i -> (char) i).collect(Collectors.toSet());
  }


}
