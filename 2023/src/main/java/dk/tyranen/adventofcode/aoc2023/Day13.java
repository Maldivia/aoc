package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 extends Challenge {
  public Result challenge() {
    List<Block> blocks = readBlocks();

    long part1 = blocks.stream().mapToLong(s -> findMirror(s, 0)).sum();
    long part2 = blocks.stream().mapToLong(s -> findMirror(s, 1)).sum();
    return new Result(part1, part2);
  }

  private List<Block> readBlocks() {
    List<Block> blocks = new ArrayList<>();

    Block block = new Block(new ArrayList<>());
    for (String line : getInput()) {
      if (line.isBlank()) {
        blocks.add(block);
        block = new Block(new ArrayList<>());
      }
      else {
        block.lines.add(line);
      }
    }
    blocks.add(block);

    return blocks;
  }

  private long findMirror(Block block, int smudges) {
    return (block.findMirror(smudges) * 100L) + (block.transpose().findMirror(smudges));
  }

  record Block(List<String> lines) {
    public Block transpose() {
      String[] rotated = new String[lines.getFirst().length()];
      Arrays.fill(rotated, "");
      for (int i = 0; i < lines.getFirst().length(); i++) {
        for (String line : lines) {
          rotated[i] += line.charAt(i);
        }
      }
      return new Block(List.of(rotated));
    }

    int findMirror(int smudges) {
      for (int i = 1; i < lines().size(); i++) {
        int len = Math.min(i, lines.size() - i);
        var front = String.join("", lines.subList(i - len, i).reversed());
        var back = String.join("", lines.subList(i, i + len));

        int diff = 0;
        for (int j = 0; j < front.length(); j++) {
          if (front.charAt(j) != back.charAt(j)) {
            if (++diff > smudges) break;
          }
        }
        if (diff == smudges) return i;
      }
      return 0;
    }
  }
}

