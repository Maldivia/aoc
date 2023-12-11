package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Day11 extends Challenge {
  public Result challenge() {
    char[][] starMap = getInput()
        .stream()
        .map(s -> s.replace('.', ' '))
        .map(s -> s.isBlank() ? s.replace(' ', '@') : s)
        .map(String::toCharArray)
        .toArray(char[][]::new);

    outer:
    for (int x = 0; x < starMap[0].length; x++) {
      for (char[] chars : starMap) {
        if (chars[x] == '#') {
          continue outer;
        }
      }
      for (int y = 0; y < starMap.length; y++) {
        starMap[y][x] = '@';
      }
    }

    List<Pos> galaxies = new ArrayList<>();
    for (int x = 0; x < starMap[0].length; x++) {
      for (int y = 0; y < starMap.length; y++) {
        if (starMap[y][x] == '#') {
          galaxies.add(new Pos(x, y));
        }
      }
    }

    long part1 = getDistance(starMap, galaxies, 2);
    long part2 = getDistance(starMap, galaxies, 1_000_000);


    return new Result(part1, part2);
  }

  private static long getDistance(char[][] starMap, List<Pos> galaxies, int expansion) {
    long sum = 0;
    for (int i = 0; i < galaxies.size() - 1; i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        int startX = Math.min(galaxies.get(i).x, galaxies.get(j).x);
        int endX = Math.max(galaxies.get(i).x, galaxies.get(j).x);
        int startY = Math.min(galaxies.get(i).y, galaxies.get(j).y);
        int endY = Math.max(galaxies.get(i).y, galaxies.get(j).y);

        long dist = 0;
        for (int x = startX+1; x <= endX; x++) dist += starMap[startY][x] == '@' ? expansion : 1;
        for (int y = startY+1; y <= endY; y++) dist += starMap[y][startX] == '@' ? expansion : 1;

        sum += dist;
      }
    }
    return sum;
  }

  record Pos(int x,int y) {}
}

