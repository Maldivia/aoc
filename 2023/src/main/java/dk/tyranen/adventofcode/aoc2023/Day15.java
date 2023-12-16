package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day15 extends Challenge {
  public Result challenge() {
    int part1 = Stream.of(getInput().getFirst().split(","))
        .mapToInt(s -> s.chars().reduce(0, (l, r) -> ((l + r) * 17) % 256))
        .sum();

    var map = IntStream.range(0, 256).mapToObj(i -> new ArrayList<Lens>()).toList();
    Stream.of(getInput().getFirst().split(","))
        .map(Lens::parse)
        .forEachOrdered(lens -> {
          if (lens.focalLength == -1) {
            map.get(lens.hashCode()).remove(lens);
          }
          else {
            ArrayList<Lens> slot = map.get(lens.hashCode());
            if (slot.contains(lens)) {
              slot.set(slot.indexOf(lens), lens);
            }
            else {
              slot.add(lens);
            }
          }
        });

    int part2 = 0;
    for (int i = 0; i < map.size(); i++) {
      ArrayList<Lens> slot = map.get(i);
      for (int j = 0; j < slot.size(); j++) {
        part2 += slot.get(j).focalLength * (j + 1) * (i + 1);
      }
    }

    return new Result(part1, part2);
  }

  record Lens(String label, int focalLength) {
    static Lens parse(String str) {
      var parts = str.split("[=-]");
      return new Lens(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : -1);
    }

    @Override
    public int hashCode() {
      return label.chars().reduce(0, (l, r) -> ((l + r) * 17) % 256);
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Lens other && other.label.equals(label);
    }
  }
}

