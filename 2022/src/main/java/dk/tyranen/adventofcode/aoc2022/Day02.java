package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day02 extends Challenge {
  public Result challenge() {
    int part1 = getInput().stream()
        .map(s -> s.split(" "))
        .mapToInt(s -> gameSum(Move.lookup(s[0]), Move.lookup(s[1])))
        .sum();



    int part2 = getInput().stream()
        .map(s -> s.split(" "))
        .mapToInt(s -> gameSum2(Move.lookup(s[0]), Move.lookup(s[1])))
        .sum();
    return new Result(part1, part2);
  }

  int gameSum(Move opponent, Move me) {
    if (opponent == me) return 3 + me.ordinal() + 1;
    else if ((me == Move.ROCK && opponent == Move.SCISSORS) ||
        (me == Move.PAPER && opponent == Move.ROCK) ||
        (me == Move.SCISSORS && opponent == Move.PAPER)) return 6 + me.ordinal() + 1;
    return me.ordinal() + 1;
  }

  int gameSum2(Move opponent, Move me) {
    return gameSum(opponent, Move.what.get(opponent).get(me.ordinal()));
  }

  enum Move {
    ROCK, PAPER, SCISSORS;
    static Move lookup(String s) {
      return switch (s) {
        case "A": case "X": yield ROCK; // Rock, Loose
        case "B": case "Y": yield PAPER; // Paper, Draw
        case "C": case "Z": yield SCISSORS; // Scissors, Win
        default: throw new IllegalArgumentException();
      };
    }

    static Map<Move, List<Move>> what = Map.of(
        ROCK, List.of(SCISSORS, ROCK, PAPER),
        PAPER, List.of(ROCK, PAPER, SCISSORS),
        SCISSORS, List.of(PAPER, SCISSORS, ROCK)
    );
  }
}
