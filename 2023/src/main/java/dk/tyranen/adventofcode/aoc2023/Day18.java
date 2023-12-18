package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Day18 extends Challenge {

  public Result challenge() {
    long part1 = calculateArea(getDigPolygon(ar -> Long.parseLong(ar[1]), ar -> ar[0].charAt(0)));
    long part2 = calculateArea(getDigPolygon(ar -> Long.parseLong(ar[2].substring(2, 7), 16), ar -> ar[2].charAt(7)));

    return new Result(part1, part2);
  }

  List<Pos> getDigPolygon(Function<String[], Long> lenExtract, Function<String[], Character> dirExtract) {
    List<Pos> result = new ArrayList<>();
    result.add(new Pos(0, 0));

    for (String s : getInput()) {
      String[] split = s.split(" ");
      long len = lenExtract.apply(split);
      char dir = dirExtract.apply(split);

      Pos prevPos = result.getLast();
      result.add(switch (dir) {
        case '0', 'R' -> new Pos(prevPos.x + len, prevPos.y);
        case '1', 'D' -> new Pos(prevPos.x, prevPos.y + len);
        case '2', 'L' -> new Pos(prevPos.x - len, prevPos.y);
        case '3', 'U' -> new Pos(prevPos.x, prevPos.y - len);
        default -> throw new RuntimeException("Unknown direction: " + dir);
      });
    }

    return result;
  }

  long calculateArea(List<Pos> polygon) {
    // Calculate the area of the polygon using the shoelace formula and add edge as well (Pick's theorem)
    long area = 0;
    Pos prev = polygon.getFirst();
    for (Pos pos : polygon.subList(1, polygon.size())) {
      area += prev.x * pos.y - prev.y * pos.x;
      area += Math.abs(prev.x - pos.x) + Math.abs(prev.y - pos.y);
      prev = pos;
    }
    return (area / 2) + 1;
  }

  record Pos(long x, long y) {}
}

