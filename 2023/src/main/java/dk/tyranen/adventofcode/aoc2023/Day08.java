package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day08 extends Challenge {
  public Result challenge() {
    List<Boolean> directions = getInput().getFirst().chars().mapToObj(c -> c == 'L').toList();

    Map<String, Node> nodeMap = getInput().stream().skip(2)
        .map(s -> s.split("[= (,)]+"))
        .collect(Collectors.toMap(s -> s[0], s -> new Node(s[1], s[2])));

    long part1 = findSteps("AAA", "ZZZ", nodeMap, directions);

    // They are all circular, ending with a Z-entry at same offset and then looping back to beginning
    // so calculate steps to Z-entry, and LCM them
    List<BigInteger> steps = nodeMap.keySet().stream()
        .filter(n -> n.endsWith("A"))
        .map(n -> findSteps(n, "Z", nodeMap, directions))
        .map(BigInteger::valueOf)
        .toList();

    System.out.println(steps);

    long part2 = lcm(steps).longValue();
    return new Result(part1, part2);
  }

  private int findSteps(String nodeName, String end, Map<String, Node> nodeMap, List<Boolean> directions) {
    int steps = 0;
    while (!nodeName.endsWith(end)) {
      nodeName = nodeMap.get(nodeName).next(directions.get(steps++ % directions.size()));
    }
    return steps;
  }

  private BigInteger lcm(List<BigInteger> list) {
    if (list.size() == 1) return list.getFirst();
    BigInteger a = list.getFirst();
    BigInteger b = lcm(list.subList(1, list.size()));

    return a.multiply(b).divide(a.gcd(b));
  }

  record Node(String left, String right) {
    public String next(boolean left) {
      return left ? this.left : this.right;
    }
  }
}

