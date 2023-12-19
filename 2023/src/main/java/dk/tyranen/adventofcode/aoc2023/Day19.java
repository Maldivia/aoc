package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day19 extends Challenge {

  public Result challenge() {
    Map<String, Workflow> workflows = getInput().stream()
        .takeWhile(s -> !s.isEmpty())
        .map(Workflow::parse)
        .collect(Collectors.toMap(w -> w.name, Function.identity()));

    List<Part> parts = getInput().stream()
        .dropWhile(s -> !s.isEmpty())
        .skip(1)
        .map(Part::parse)
        .toList();

    long part1 = parts.stream()
        .filter(p -> isAccepted(p, workflows))
        .mapToInt(p -> p.values().values().stream().mapToInt(Integer::intValue).sum())
        .sum();

    long part2 = findAcceptCombinations(workflows);

    return new Result(part1, part2);
  }

  private long findAcceptCombinations(Map<String, Workflow> workflows) {
    return traverseRoutes(workflows, workflows.get("in"), new ArrayList<>(), new ArrayList<>());
  }

  private long traverseRoutes(Map<String, Workflow> workflows, Workflow flow, List<Rule> accept, List<Rule> reject) {
    long result = 0;
    for (Rule rule : flow.rules()) {
      if (rule.destination.equals("A")) {
        accept.add(rule);
        result += calc(accept, reject);
        accept.removeLast();
      }
      else if (!rule.destination.equals("R")) {
        accept.add(rule);
        result += traverseRoutes(workflows, workflows.get(rule.destination), accept, reject);
        accept.removeLast();
      }
      reject.add(rule);
    }
    reject.subList(reject.size() - flow.rules().size(), reject.size()).clear();
    return result;
  }

  private long calc(List<Rule> accept, List<Rule> reject) {
    Map<String, Integer> min = new HashMap<>(Map.of("x", 1, "m", 1, "a", 1, "s", 1));
    Map<String, Integer> max = new HashMap<>(Map.of("x", 4000, "m", 4000, "a", 4000, "s", 4000));
    for (Rule rule : accept) {
      switch (rule.cmp) {
        case '<' -> max.compute(rule.var, (k, v) -> Math.min(v, rule.value - 1));
        case '>' -> min.compute(rule.var, (k, v) -> Math.max(v, rule.value + 1));
      }
    }

    for (Rule rule : reject) {
      switch (rule.cmp) {
        case '<' -> min.compute(rule.var, (k, v) -> Math.max(v, rule.value));
        case '>' -> max.compute(rule.var, (k, v) -> Math.min(v, rule.value));
      }
    }

    return (1L + max.get("x") - min.get("x")) * (1L + max.get("m") - min.get("m")) * (1L + max.get("a") - min.get("a")) * (1L + max.get("s") - min.get("s"));
  }

  private boolean isAccepted(Part p, Map<String, Workflow> workflows) {
    Workflow flow = workflows.get("in");
    flows: while (flow != null) {
      for (Rule rule : flow.rules()) {
        if (rule.predicate().test(p)) {
          switch (rule.destination) {
            case "A" -> { return true; }
            case "R" -> { return false; }
            default -> {
              flow = workflows.get(rule.destination);
              continue flows;
            }
          }
        }
      }
      throw new RuntimeException("Should not happen");
    }
    throw new RuntimeException("Should not happen");
  }

  record Workflow(String name, List<Rule> rules) {
    private static final Pattern pattern = Pattern.compile("(.*?)\\{(.*?)}");

    static Workflow parse(String line) {
      Matcher matcher = pattern.matcher(line);
      if (!matcher.matches()) {
        throw new RuntimeException("Failed to parse line: " + line);
      }
      return new Workflow(matcher.group(1), Stream.of(matcher.group(2).split(",")).map(Rule::parse).toList());
    }
  }

  record Rule(Predicate<Part> predicate, String var, char cmp, int value, String destination) {
    private static final Pattern pattern = Pattern.compile("(.*?)([<>])(.*?):(.*?)");

    static Rule parse(String line) {
      Matcher matcher = pattern.matcher(line);
      if (!matcher.matches()) {
        return new Rule(part -> true, line, '*', 0, line);
      }
      String field = matcher.group(1);
      char cmp = matcher.group(2).charAt(0);
      int value = Integer.parseInt(matcher.group(3));
      String dest = matcher.group(4);
      return cmp == '<' ?
          new Rule(part -> part.values.get(field) < value, field, cmp, value, dest) :
          new Rule(part -> part.values.get(field) > value, field, cmp, value, dest);
    }
  }

  record Part(Map<String, Integer> values) {
    static Part parse(String line) {
      return new Part(Stream.of(line.substring(1, line.length() - 1).split(","))
          .map(s -> s.split("="))
          .collect(Collectors.toMap(s -> s[0], s -> Integer.parseInt(s[1]))));
    }
  }
}

