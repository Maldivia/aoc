package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 extends Challenge {

  public Result challenge() {
    long part1 = findHeatLoss(1, 3);
    long part2 = findHeatLoss(4, 10);

    return new Result(part1, part2);
  }

  private CityBlock[][] loadCity() {
    return getInput().stream()
        .map(s -> s.chars().map(i -> i - '0').mapToObj(i -> new CityBlock(i, new HashMap<>())).toArray(CityBlock[]::new))
        .toArray(CityBlock[][]::new);
  }

  int findHeatLoss(int minStep, int maxStep) {
    CityBlock[][] city = loadCity();
    Deque<Path> queue = new ArrayDeque<>();
    queue.add(new Path(0, 0, 0, Dir.START));

    while (!queue.isEmpty()) {
      Path p = queue.removeFirst();

      for (Dir dir : p.move().validDirs()) {
        for (int i = minStep; i <= maxStep; i++) {
          int newX = p.x + i * dir.xOffset;
          int newY = p.y + i * dir.yOffset;
          if (newX < 0 || newX >= city[0].length || newY < 0 || newY >= city.length)
            continue;

          int loss = 0;
          for (int j = 1; j <= i; j++) {
            int newX2 = p.x + j * dir.xOffset;
            int newY2 = p.y + j * dir.yOffset;
            loss += city[newY2][newX2].heatLoss;
          }

          CityBlock nextBlock = city[newY][newX];
          int newDistance = p.heatLoss + loss;
          if (newDistance < nextBlock.totalLoss.getOrDefault(dir, Integer.MAX_VALUE)) {
            nextBlock.totalLoss.put(dir, newDistance);
            queue.add(new Path(newX, newY, newDistance, dir));
          }
        }
      }
    }

    CityBlock endBlock = city[city.length-1][city[0].length-1];
    return endBlock.totalLoss().values().stream().mapToInt(Integer::intValue).min().orElseThrow();
  }

  enum Dir {
    DOWN(0,+1), LEFT(-1,0), UP(0,-1), RIGHT(1,0), START(0,0);

    private final int xOffset;
    private final int yOffset;

    Dir(int xOffset, int yOffset) {
      this.xOffset = xOffset;
      this.yOffset = yOffset;
    }

    List<Dir> validDirs() {
      return switch (this) {
        case UP, DOWN -> List.of(LEFT, RIGHT);
        case LEFT, RIGHT -> List.of(UP, DOWN);
        case START -> List.of(RIGHT, DOWN);
      };
    }
  }

  record Path(int x, int y, int heatLoss, Dir move) { }
  record CityBlock(int heatLoss, Map<Dir, Integer> totalLoss) {}
}

