package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Day01 extends Challenge {
  public Result challenge() {
    int sum = 0;
    List<Integer> list = new ArrayList<>();
    for (String line : getInput()) {
      if (line.isBlank()) {
        list.add(sum);
        sum = 0;
      }
      else {
        sum += Integer.parseInt(line);
      }
    }
    list.add(sum);
    int part1 = list.stream().mapToInt(Integer::intValue).max().getAsInt();
    int part2 = list.stream().sorted(Comparator.reverseOrder()).mapToInt(Integer::intValue).limit(3).sum();
    return new Result(part1, part2);
  }
}
