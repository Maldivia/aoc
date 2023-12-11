package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 extends Challenge {
  public Result challenge() {
    List<Hand> list = getInput().stream()
        .map(this::toHand)
        .sorted((Comparator.comparing(Hand::value).thenComparing(Comparator.comparing(Hand::hand).reversed())).reversed())
        .toList();
    System.out.println(list);

    long part1 = IntStream.range(0, list.size())
        .map(i -> list.get(i).bid() * (i+1))
        .sum();

    List<Hand> list2 = getInput().stream()
        .map(this::toJokerHand)
        .sorted((Comparator.comparing(Hand::value).thenComparing(Comparator.comparing(Hand::hand).reversed())).reversed())
        .toList();
    System.out.println(list2);

    long part2 = IntStream.range(0, list2.size())
        .map(i -> list2.get(i).bid() * (i+1))
        .sum();


    return new Result(part1, part2);
  }

  private Hand toJokerHand(String line) {
    return toHand(line.replace('J', '0'));
  }
  private Hand toHand(String line) {
    String[] data = line.replace('A', 'E').replace('K', 'D').replace('Q', 'C').replace('J', 'B').replace('T', 'A').split(" ");
    String hand = data[0];
    int bid = Integer.parseInt(data[1]);
    return new Hand(hand, findValue(hand), bid);
  }

  private HandValue findValue(String hand) {
    Map<Character, Integer> map = hand.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.summingInt(c -> 1)));
    int jokerCount = map.getOrDefault('0', 0);
    List<Integer> values = map.entrySet().stream().filter(e -> e.getKey() != '0').map(Map.Entry::getValue).sorted(Comparator.reverseOrder()).toList();
    return jokerCount == 5 ? HandValue.FIVE
        : (values.size() == 1) ? HandValue.lookup(values.get(0) + jokerCount, 0)
        : HandValue.lookup(values.get(0) + jokerCount, values.get(1));
  }


  enum HandValue {
    FIVE, FOUR, FULLHOUSE, THREE, TWOPAIR, PAIR, HIGH;
    static HandValue lookup(int card1, int card2) {
      return switch (card1) {
        case 5 -> HandValue.FIVE;
        case 4 -> HandValue.FOUR;
        case 3 -> card2 == 2 ? HandValue.FULLHOUSE : HandValue.THREE;
        case 2 -> card2 == 2 ? HandValue.TWOPAIR : HandValue.PAIR;
        default -> HandValue.HIGH;
      };
    }
  }

  record Hand(String hand, HandValue value, int bid) {}
}

