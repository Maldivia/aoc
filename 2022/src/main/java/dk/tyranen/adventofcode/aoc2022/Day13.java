package dk.tyranen.adventofcode.aoc2022;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 extends Challenge {
  public Result challenge() {
    String fullInput = String.join("\n", getInput());

    List<Package> packages = Pattern.compile("""
            (\\[.*?\\])
            (\\[.*?\\])
            """).matcher(fullInput+"\n\n")
        .results()
        .map(m -> new Package(parseData(m.group(1)), parseData(m.group(2))))
        .toList();

    int part1 = IntStream.range(0, packages.size())
        .filter(i -> packages.get(i).left.compareTo(packages.get(i).right) <= 0)
        .map(i -> i + 1)
        .sum();

    Data decoderOne = parseData("[[2]]");
    Data decoderTwo = parseData("[[6]]");

    List<Data> list = Stream.concat(Stream.of(decoderOne, decoderTwo), packages.stream().flatMap(p -> Stream.of(p.left, p.right)))
        .sorted()
        .toList();

    int part2 = ((list.indexOf(decoderOne) + 1) * (list.indexOf(decoderTwo) + 1));

    return new Result(part1, part2);
  }

  private Data parseData(String data) {
    char[] chars = data.toCharArray();
    return parseNext(chars, new AtomicInteger(0));
  }

  private Data parseNext(char[] chars, AtomicInteger offset) {
    if (chars[offset.get()] == '[') {
      offset.incrementAndGet();
      List<Data> list = new ArrayList<>();
      while (chars[offset.get()] != ']') {
        list.add(parseNext(chars, offset));
        if (chars[offset.get()] == ',') {
          offset.incrementAndGet();
        }
      }
      offset.incrementAndGet();
      return new Data(-1, list);
    } else {
      int offsetStart = offset.get();
      while (chars[offset.get()] != ',' && chars[offset.get()] != ']') {
        offset.incrementAndGet();
      }
      try {
        int value = Integer.parseInt(new String(chars, offsetStart, offset.get() - offsetStart));
        return new Data(value, null);
      }
      catch (NumberFormatException e) {
        throw new RuntimeException("Failed to parse string: " + new String(chars) + " at offset " + offset.get(), e);
      }
    }
  }

  record Package(Data left, Data right) {}
  record Data(int value, List<Data> listValues) implements Comparable<Data> {
    boolean isList() {
      return value == -1;
    }

    @Override
    public int compareTo(Data other) {
      if (!isList() && !other.isList()) {
        return Integer.compare(value, other.value);
      }
      else if (!isList()) {
        return new Data(-1, List.of(this)).compareTo(other);
      }
      else if (!other.isList()) {
        return this.compareTo(new Data(-1, List.of(other)));
      }
      else {
        int size = Math.min(listValues().size(), other.listValues.size());
        for (int i = 0; i < size; i++) {
          int res = listValues().get(i).compareTo(other.listValues().get(i));
          if (res != 0) {
            return res;
          }
        }

        return Integer.compare(listValues.size(), other.listValues.size());
      }
    }
  }
}