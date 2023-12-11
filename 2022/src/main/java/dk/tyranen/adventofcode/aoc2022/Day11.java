package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 extends Challenge {
  public Result challenge() {
    String fullInput = String.join("\n", getInput());


    long part1 = monkeyBusiness(fullInput, 20, BigInteger.valueOf(3));
    long part2 = monkeyBusiness(fullInput, 10_000, BigInteger.valueOf(1));

    return new Result(part1, part2);
  }

  private long monkeyBusiness(String fullInput, int roundCount, BigInteger worryReduction) {
    List<List<Monkey>> rounds = new ArrayList<>();
    rounds.add(Pattern.compile("""
            Monkey (\\d+):
              Starting items: ([0-9, ]+)
              Operation: new = (old|[0-9]+) ([*+]) (old|[0-9]+)
              Test: divisible by (\\d+)
                If true: throw to monkey (\\d+)
                If false: throw to monkey (\\d+)""")
        .matcher(fullInput)
        .results()
        .map(Monkey::create)
        .toList());

    BigInteger COMMON_DIVISOR = rounds.getFirst().stream()
        .map(m -> m.test)
        .distinct()
        .reduce(BigInteger.ONE, BigInteger::multiply);

    for (int i = 1; i <= roundCount; i++) {
      List<Monkey> monkeys = rounds.get(i - 1);
      rounds.add(monkeys.stream().map(Monkey::copy).toList());
      for (Monkey monkey : monkeys) {
        for (BigInteger item : monkey.items) {
          monkey.count.incrementAndGet();
          BigInteger result = monkey.operation.apply(item).divide(worryReduction).mod(COMMON_DIVISOR);

          if (result.mod(monkey.test).equals(BigInteger.ZERO)) {
            rounds.get(monkey.trueMonkey > monkey.id ? i - 1 : i).get(monkey.trueMonkey).items.add(result);
          } else {
            rounds.get(monkey.falseMonkey > monkey.id ? i - 1 : i).get(monkey.falseMonkey).items.add(result);
          }
        }
      }
    }

    return rounds.get(roundCount).stream()
        .mapToLong(m -> -m.count.get())
        .sorted()
        .limit(2)
        .reduce(1, (a, b) -> a * b);
  }

  record Monkey(int id, List<BigInteger> items, UnaryOperator<BigInteger> operation, BigInteger test, int trueMonkey, int falseMonkey, AtomicLong count) {
    public Monkey copy() {
      return new Monkey(id, new ArrayList<>(), operation, test, trueMonkey, falseMonkey, count);
    }

    public static Monkey create(MatchResult matcher) {
      return new Monkey(Integer.parseInt(matcher.group(1)),
          Pattern.compile(", ").splitAsStream(matcher.group(2)).map(BigInteger::new).collect(Collectors.toList()),
          toOperation(matcher.group(3), matcher.group(4).charAt(0), matcher.group(5)),
          new BigInteger(matcher.group(6)),
          Integer.parseInt(matcher.group(7)),
          Integer.parseInt(matcher.group(8)),
          new AtomicLong(0));
    }

    static UnaryOperator<BigInteger> toOperation(String left, char op, String right) {
      UnaryOperator<BigInteger> leftOp = toUnary(left);
      UnaryOperator<BigInteger> rightOp = toUnary(right);
      if (op == '+') return i -> leftOp.apply(i).add(rightOp.apply(i));
      if (op == '*') return i -> leftOp.apply(i).multiply(rightOp.apply(i));
      throw new RuntimeException("Unknown operation: " + op);
    }

    static UnaryOperator<BigInteger> toUnary(String str) {
      if ("old".equals(str)) return i -> i;
      BigInteger value = new BigInteger(str);
      return i -> value;
    }
  }
}