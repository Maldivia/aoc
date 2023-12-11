package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day09 extends Challenge {
  public Result challenge() {
    long part1 = countTailSteps(2);
    long part2 = countTailSteps(10);

    return new Result(part1, part2);
  }

  private long countTailSteps(int length) {
    Set<Pos> tailPos = new HashSet<>();
    List<Pos> rope = Stream.generate(() -> new Pos(0, 0))
        .limit(length)
        .collect(Collectors.toCollection(ArrayList::new));
    tailPos.add(rope.getLast());

    for (String s : getInput()) {
      String[] data = s.split(" ");
      String dir = data[0];
      int steps = Integer.parseInt(data[1]);

      for (int i = 0; i < steps; i++) {
        Pos head = rope.getFirst();
        rope.set(0, switch (dir) {
          case "U" -> new Pos(head.x, head.y + 1);
          case "D" -> new Pos(head.x, head.y - 1);
          case "L" -> new Pos(head.x - 1, head.y);
          case "R" -> new Pos(head.x + 1, head.y);
          default -> throw new IllegalStateException("Unexpected value: " + dir);
        });

        for (int x = 1; x < rope.size(); x++) {
          rope.set(x, updatePos(rope.get(x - 1), rope.get(x)));
        }
        tailPos.add(rope.getLast());
      }
    }
    return tailPos.size();
  }

  private boolean adjacent(Pos pos1, Pos pos2) {
    return Math.abs(pos1.x - pos2.x) <= 1 && Math.abs(pos1.y - pos2.y) <= 1;
  }

  private Pos updatePos(Pos head, Pos tail) {
    return (adjacent(head, tail)) ? tail : new Pos(tail.x + Integer.compare(head.x, tail.x), tail.y + Integer.compare(head.y, tail.y));
  }

  record Pos(int x, int y) {}
}