package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day06 extends Challenge {
  public Result challenge() {
    long part1 = getWins(getInput().get(0), getInput().get(1));
    long part2 = getWins(getInput().get(0).replace(" ", ""), getInput().get(1).replace(" ", ""));

    return new Result(part1, part2);
  }

  private long getWins(String time, String distance) {
    String[] times = time.split("[: ]+");
    String[] distances = distance.split("[: ]+");
    List<Race> list = IntStream.range(1, times.length)
        .mapToObj(i -> new Race(Long.parseLong(times[i]), Long.parseLong(distances[i])))
        .toList();

    return list.stream().mapToLong(this::wins).reduce(1L, Math::multiplyExact);
  }

  private long wins(Race race) {
    long push = 1;
    while ((push * (race.time - push)) < race.distance) push++;
    return race.time - (push * 2) + 1;
  }

  record Race(long time, long distance) {}
}

