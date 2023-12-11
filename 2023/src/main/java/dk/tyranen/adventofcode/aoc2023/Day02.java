package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day02 extends Challenge {
  public Result challenge() {
    var cubePattern = Pattern.compile("(\\d+) (red|blue|green)");
    Map<Integer, Map<String, Integer>> cubes = getInput().stream()
        .map(line -> line.split(":"))
        .collect(Collectors.toMap(
            data -> Integer.parseInt(data[0].split(" ")[1]),
            data -> cubePattern.matcher(data[1]).results()
                .collect(Collectors.toMap(
                    m -> m.group(2),
                    m -> Integer.parseInt(m.group(1)),
                    Math::max
                ))
        ));

    int part1 = cubes.entrySet()
        .stream().filter(e ->
            e.getValue().get("red") <= 12 &&
                e.getValue().get("green") <= 13 &&
                e.getValue().get("blue") <= 14)
        .mapToInt(Map.Entry::getKey)
        .sum();

    int part2 = cubes.values()
        .stream()
        .mapToInt(m -> m.values().stream().reduce(1, (a, b) -> a * b))
        .sum();
    return new Result(part1, part2);
  }
}
