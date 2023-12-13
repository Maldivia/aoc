package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends Challenge {
  public Result challenge() {
    long part1 = getInput().stream()
        .map(Puzzle::parse)
        .mapToLong(this::testPuzzle)
        .sum();

    long part2 = getInput().stream()
        .map(Puzzle::prep)
        .map(Puzzle::parse)
        .mapToLong(this::testPuzzle)
        .sum();

    return new Result(part1, part2);
  }

  static LongAdder counter = new LongAdder();
  private long testPuzzle(Puzzle puzzle) {
    return countArrangements(puzzle, new HashMap<>(), new CacheKey(0, 0, 0));
  }

  static long countArrangements(Puzzle puzzle, Map<CacheKey, Long> cache, CacheKey state) {
    counter.increment();
    if (cache.containsKey(state)) {
      return cache.get(state);
    }

    if (state.offset >= puzzle.condition.length() || state.group > puzzle.groups.size()) {
      // end of the condition string or out of groups, check if we have used all the groups
      long result = (state.group == puzzle.groups.size() && state.groupOffset == 0) ? 1L : 0L;
      cache.put(state, result);
      return result;
    }

    long sum = 0;
    char c = puzzle.condition.charAt(state.offset);

    // find groups
    if (c != '.' && state.group < puzzle.groups.size() && state.groupOffset < puzzle.groups.get(state.group)) {
      sum += countArrangements(puzzle, cache, new CacheKey(state.offset + 1, state.group, state.groupOffset + 1));
    }

    // find separators
    if (c != '#') {
      if (state.group < puzzle.groups.size() && state.groupOffset == puzzle.groups.get(state.group)) {
        sum += countArrangements(puzzle, cache, new CacheKey(state.offset + 1, state.group + 1, 0));
      }
      else if (state.groupOffset == 0) {
        sum += countArrangements(puzzle, cache, new CacheKey(state.offset + 1, state.group, state.groupOffset));
      }
    }

    cache.put(state, sum);
    return sum;
  }

  record CacheKey(int offset, int group, int groupOffset) {}
  record Puzzle(String condition, List<Integer> groups) {
    Puzzle {
      condition = condition + "."; // append a . so we can complete last group
    }
    static Puzzle parse(String line) {
      var parts = line.split(" ");
      var condition = parts[0];
      List<Integer> options = Stream.of(parts[1].split(",")).map(Integer::parseInt).toList();
      return new Puzzle(condition, options);
    }

    public static String prep(String input) {
      var split = input.split(" ");
      return Stream.generate(() -> split[0]).limit(5).collect(Collectors.joining("?")) + " " + Stream.generate(() -> split[1]).limit(5).collect(Collectors.joining(","));
    }
  }
}

