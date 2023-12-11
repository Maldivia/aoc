package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.regex.Pattern;

public class Day01 extends Challenge {
  public Result challenge() {
    int part1 = getInput().stream()
        .map(line -> line.replaceAll("[^0-9]", "").toCharArray())
        .mapToInt(a -> Integer.parseInt(a[0] +""+ a[a.length - 1]))
        .sum();

    var pattern = Pattern.compile("(?=(one|two|three|four|five|six|seven|eight|nine|([0-9]))).");
    int part2 = getInput().stream()
        .map(line -> pattern.matcher(line).results().map(m -> toInt(line.substring(m.start(1), m.end(1)))).toArray())
        .mapToInt(a -> Integer.parseInt(a[0] +""+ a[a.length - 1]))
        .sum();
    return new Result(part1, part2);
  }

  static int toInt(String str) {
    return switch (str) {
      case "one" -> 1;
      case "two" -> 2;
      case "three" -> 3;
      case "four" -> 4;
      case "five" -> 5;
      case "six" -> 6;
      case "seven" -> 7;
      case "eight" -> 8;
      case "nine" -> 9;
      default -> Integer.parseInt(str);
    };
  }
}
