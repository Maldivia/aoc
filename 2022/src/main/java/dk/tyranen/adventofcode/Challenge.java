package dk.tyranen.adventofcode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public abstract class Challenge {
  private final List<String> input;
  public Challenge() {
    Class<?> klazz = this.getClass();
    input = new BufferedReader(new InputStreamReader(klazz.getResourceAsStream(klazz.getSimpleName() + ".txt"))).lines().toList();
  }

  public List<String> getInput() {
    return input;
  }

  public abstract Result challenge();

  public void main() {
    Result result = challenge();
    System.out.println(STR."Part 1: \{result.part1()}\nPart 2: \{result.part2()}");
  }

  public record Result(Object part1, Object part2) { }
}
