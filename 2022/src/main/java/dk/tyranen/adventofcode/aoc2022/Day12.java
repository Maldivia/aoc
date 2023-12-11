package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day12 extends Challenge {
  static Node start = null;
  static Node end = null;
  public Result challenge() {
    char[][] input = getInput().stream()
        .map(String::toCharArray)
        .toArray(char[][]::new);

    Node[][] map = new Node[input.length][input[0].length];
    for (int i = 0; i < input.length; i++) {
      for (int j = 0; j < input[i].length; j++) {
        char elevation = input[i][j];
        if (elevation == 'S')
          start = map[i][j] = new Node(i, j, 'a', new ArrayList<>(), new AtomicInteger(Integer.MAX_VALUE));
        else if (elevation == 'E')
          end = map[i][j] = new Node(i, j, 'z', new ArrayList<>(), new AtomicInteger(Integer.MAX_VALUE));
        else
          map[i][j] = new Node(i, j, elevation, new ArrayList<>(), new AtomicInteger(Integer.MAX_VALUE));
      }
    }

    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        Node node = map[i][j];
        if (i > 0) node.testAndAdd(map[i-1][j]);
        if (j > 0) node.testAndAdd(map[i][j-1]);
        if (i < (map.length - 1)) node.testAndAdd(map[i+1][j]);
        if (j < (map[i].length - 1)) node.testAndAdd(map[i][j+1]);
      }
    }

    start.distance.set(0);
    shortestPath(List.of(start));
    int part1 = end.distance.get();

    // Reset distances
    for (Node[] nodes : map) {
      for (Node node : nodes) {
        node.distance.set(node.elevation == 'a' ? 0 : Integer.MAX_VALUE);
      }
    }

    shortestPath(Stream.of(map)
        .flatMap(Stream::of)
        .filter(node -> node.elevation == 'a')
        .toList());
    int part2 = end.distance.get();

    return new Result(part1, part2);
  }

  private void shortestPath(List<Node> startNodes) {
    Deque<Node> queue = new ArrayDeque<>(startNodes);

    while (!queue.isEmpty()) {
      Node node = queue.removeFirst();
      for (Node adjacent : node.adjacent) {
        int newDist = node.distance.get() + 1;
        if (adjacent.distance.get() > newDist) {
          adjacent.distance.set(newDist);
          queue.add(adjacent);
        }
      }
    }
  }

  record Node(int x, int y, char elevation, List<Node> adjacent, AtomicInteger distance) {
    @Override
    public int hashCode() {
      return x * 31 + y;
    }

    public void testAndAdd(Node other) {
      if (elevation + 1 >= other.elevation) {
        adjacent.add(other);
      }
    }
  }
}