package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day07 extends Challenge {
  public Result challenge() {
    Dir root = Dir.newDir("/", null);
    List<Dir> allDirs = new ArrayList<>();

    allDirs.add(root);

    Dir cwd = root;
    for (String s : getInput()) {
      if (s.startsWith("$ cd")) {
        String path = s.substring(5);
        cwd = switch (path) {
          case ".." -> cwd.parent;
          case "/" -> root;
          default -> cwd.dirs.get(path);
        };
      }
      else if (s.startsWith("$ ls")) {
        // ignore
      }
      else if (s.startsWith("dir")) {
        allDirs.add(Dir.newDir(s.substring(4), cwd));
      }
      else {
        String[] data = s.split(" ");
        File.newFile(cwd, data[1], Long.parseLong(data[0]));
      }
    }

    long part1 = allDirs.stream()
        .mapToLong(Dir::recursiveSize)
        .filter(size -> size <= 100000L)
        .sum();

    long toFree = 30000000 - (70000000 - root.recursiveSize());
    long part2 = allDirs.stream()
        .mapToLong(Dir::recursiveSize)
        .filter(size -> size >= toFree)
        .min()
        .getAsLong();

    return new Result(part1, part2);
  }

  record Dir(String name, Map<String, Dir> dirs, Map<String, File> files, Dir parent) {
    static Dir newDir(String name, Dir parent) {
      Dir dir = new Dir(name, new HashMap<>(), new HashMap<>(), parent);
      if (parent != null) {
        parent.dirs.put(name, dir);
      }
      return dir;
    }

    long recursiveSize() {
      return files.values().stream().mapToLong(File::size).sum() + dirs.values().stream().mapToLong(Dir::recursiveSize).sum();
    }
  }
  record File(String name, long size) {
    static File newFile(Dir dir, String name, long size) {
      File file = new File(name, size);
      dir.files.put(name, file);
      return file;
    }
  }
}

