package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 extends Challenge {
  @Override
  public Result challenge() {
    List<Card> cards = getInput().stream()
        .map(a -> {
          String[] data = a.split("\\|");
          var winningNumbers = getNumbers(data[0].split(":")[1]);
          var numbers = getNumbers(data[1]);
          int winningCount = winningNumbers.size();
          winningNumbers.removeAll(numbers);
          return new Card(winningCount - winningNumbers.size());
        })
        .toList();

    int part1 = cards.stream()
        .mapToInt(card -> 1 << (card.wins - 1))
        .sum();

    int part2 = 0;
    for (int i = 0; i < cards.size(); i++) {
      for (int j = 0; j < cards.get(i).wins; j++) {
        cards.get(i + j + 1).copies += cards.get(i).copies;
      }
      part2 += cards.get(i).copies;
    }

    return new Result(part1, part2);
  }

  private final Pattern pattern = Pattern.compile("\\d+");
  private Set<Integer> getNumbers(String input) {
    return pattern.matcher(input).results()
        .map(m -> Integer.parseInt(m.group()))
        .collect(Collectors.toSet());
  }

  static class Card {
    final int wins;
    int copies;

    Card(int wins) {
      this.wins = wins;
      copies = 1;
    }
  }
}
