package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 extends Challenge {
  @Override
  public Result challenge() {
    List<String> list = getInput().stream()
        .takeWhile(s -> !s.isBlank())
        .toList()
        .reversed();

    int stackCount = list.getFirst().replace(" ", "").length();
    List<List<String>> stacks = new ArrayList<>();
    for (int i = 0; i < stackCount; i++) {
      stacks.add(new ArrayList<>());
    }

    System.out.println(stacks);

    for (String s : list.subList(1, list.size())) {
      for (int i = 0; i < stackCount; i++) {
        if (s.length() > ((i * 4) + 1) && !s.substring(i * 4 + 1, i * 4 + 2).isBlank()) {
          stacks.get(i).add(s.substring(i * 4 + 1, i * 4 + 2));
        }
      }
    }

    List<ArrayList<String>> part1Stacks = stacks.stream().map(ArrayList::new).toList();
    getInput().stream()
        .skip(list.size() + 1)
        .forEach(s -> {
          String[] split = s.split("[^0-9]+");
          int count = Integer.parseInt(split[1]);
          var from = part1Stacks.get(Integer.parseInt(split[2]) - 1);
          var to = part1Stacks.get(Integer.parseInt(split[3]) - 1);
          for (int i = 0; i < count; i++) {
            to.add(from.removeLast());
          }
        });

    String part1 = part1Stacks.stream()
        .map(List::getLast)
        .collect(Collectors.joining());

    List<ArrayList<String>> part2Stacks = stacks.stream().map(ArrayList::new).toList();
    getInput().stream()
        .skip(list.size() + 1)
        .forEach(s -> {
          String[] split = s.split("[^0-9]+");
          int count = Integer.parseInt(split[1]);
          var from = part2Stacks.get(Integer.parseInt(split[2]) - 1);
          var to = part2Stacks.get(Integer.parseInt(split[3]) - 1);
          List<String> sub = from.subList(from.size() - count, from.size());
          to.addAll(sub);
          sub.clear();
        });

    String part2 = part2Stacks.stream()
        .map(List::getLast)
        .collect(Collectors.joining());

    return new Result(part1, part2);
  }
}
