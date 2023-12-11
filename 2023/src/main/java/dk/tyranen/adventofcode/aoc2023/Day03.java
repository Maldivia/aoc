package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day03 extends Challenge {
  @Override
  public Result challenge() {
    List<String> lines = getInput();
    Pattern part1Pattern = Pattern.compile("[0-9]+");
    Pattern part2Pattern = Pattern.compile("\\*");

    int part1 = IntStream.range(0, lines.size())
        .map(lineNo -> part1Pattern.matcher(lines.get(lineNo))
            .results()
            .filter(m -> hasAdjacentSymbols(lines, lineNo, m.start(), m.end()))
            .mapToInt(m -> Integer.parseInt(m.group()))
            .sum())
        .sum();

    int part2 = IntStream.range(0, lines.size())
        .map(lineNo -> part2Pattern.matcher(lines.get(lineNo))
            .results()
            .mapToInt(m -> getGearValue(lines, lineNo, m.start(), m.end()))
            .sum())
        .sum();
    return new Result(part1, part2);
  }

  static boolean hasAdjacentSymbols(List<String> lines, int line, int start, int end) {
    return !(getAdjacent(lines, line - 1, start, end)
        + getAdjacent(lines, line, start, end)
        + getAdjacent(lines, line + 1, start, end))
        .replaceAll("[0-9. ]", "")
        .isEmpty();
  }

  static String getAdjacent(List<String> lines, int line, int start, int end) {
    return (line >= 0 && line < lines.size())
        ? lines.get(line).substring(Math.max(0, start - 1), Math.min(end + 1, lines.get(line).length()))
        : "";
  }

  static int getGearValue(List<String> lines, int line, int start, int end) {
    List<Integer> numbers = new ArrayList<>();
    numbers.addAll(getGearNumbers(lines, line - 1, start, end));
    numbers.addAll(getGearNumbers(lines, line, start, end));
    numbers.addAll(getGearNumbers(lines, line + 1, start, end));

    return (numbers.size() == 2) ? numbers.get(0) * numbers.get(1) : 0;
  }

  private static List<Integer> getGearNumbers(List<String> lines, int lineNo, int start, int end) {
    if (lineNo < 0 || lineNo >= lines.size()) {
      return List.of();
    }
    String line = lines.get(lineNo);
    List<Integer> numbers = new ArrayList<>();
    if (Character.isDigit(line.charAt(start))) {
      numbers.add(Integer.parseInt(line.substring(findStart(line, start), findEnd(line, end))));
    }
    else {
      if (start > 0 && Character.isDigit(line.charAt(start - 1))) {
        numbers.add(Integer.parseInt(line.substring(findStart(line, start - 1), start)));
      }
      if (end < line.length() && Character.isDigit(line.charAt(end))) {
        numbers.add(Integer.parseInt(line.substring(end, findEnd(line, end))));
      }
    }
    return numbers;
  }

  static int findStart(String line, int start) {
    while (start > 0 && Character.isDigit(line.charAt(start - 1))) {
      start--;
    }
    return start;
  }

  static int findEnd(String line, int end) {
    while (end < line.length() && Character.isDigit(line.charAt(end))) {
      end++;
    }
    return end;
  }
}
