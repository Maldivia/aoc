package dk.tyranen.adventofcode.aoc2023;

import dk.tyranen.adventofcode.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 extends Challenge {
  @Override
  public Result challenge() {
    List<String> input = getInput();
    Iterator<String> it = input.iterator();
    List<Seed> seeds = Arrays.stream(it.next().substring(7).split(" ")).map(Long::parseLong).map(l -> new Seed(l, l + 1)).toList();
    it.next();

    List<Mapper<Seed, Soil>> seedSoil = readMappers(it, Seed::new, Soil::new);
    List<Mapper<Soil, Fertilizer>> soilFertilizer = readMappers(it, Soil::new, Fertilizer::new);
    List<Mapper<Fertilizer, Water>> fertilizerWater = readMappers(it, Fertilizer::new, Water::new);
    List<Mapper<Water, Light>> waterLight = readMappers(it, Water::new, Light::new);
    List<Mapper<Light, Temperature>> lightTemperature = readMappers(it, Light::new, Temperature::new);
    List<Mapper<Temperature, Humidity>> temperatureHumidity = readMappers(it, Temperature::new, Humidity::new);
    List<Mapper<Humidity, Location>> humidityLocation = readMappers(it, Humidity::new, Location::new);

    Function<Stream<Seed>, Long> func = str -> str
        .flatMap(seed -> doMapping(seedSoil, seed))
        .flatMap(soil -> doMapping(soilFertilizer, soil))
        .flatMap(fertilizer -> doMapping(fertilizerWater, fertilizer))
        .flatMap(water -> doMapping(waterLight, water))
        .flatMap(light -> doMapping(lightTemperature, light))
        .flatMap(temperature -> doMapping(temperatureHumidity, temperature))
        .flatMap(humidity -> doMapping(humidityLocation, humidity))
        .min(Comparator.comparingLong(Location::start))
        .map(Location::start)
        .get();

    long part1 = func.apply(seeds.stream());
    Stream<Seed> part2Stream = IntStream.range(0, seeds.size() / 2)
        .mapToObj(i -> new Seed(seeds.get(i*2).start(), seeds.get(i*2).start() + seeds.get(i*2 + 1).start()));

    long part2 = func.apply(part2Stream);

    return new Result(part1, part2);
  }

  private <SOURCE extends Value, DEST extends Value> Stream<DEST> doMapping(List<Mapper<SOURCE, DEST>> mappers, SOURCE source) {
    List<SOURCE> sources = List.of(source);
    List<DEST> destinations = new ArrayList<>();
    for (Mapper<SOURCE, DEST> mapper : mappers) {
      List<SOURCE> newSources = new ArrayList<>();
      for (SOURCE src : sources) {
        Mapper.Result<SOURCE, DEST> result = mapper.map(src);
        newSources.addAll(result.sources());
        if (result.destination() != null) {
          destinations.add(result.destination());
        }
      }
      sources = newSources;
      if (sources.isEmpty()) {
        break;
      }
    }
    return destinations.stream();
  }

  private <SOURCE extends Value, DEST extends Value> List<Mapper<SOURCE, DEST>> readMappers(Iterator<String> it, ValueCreator<SOURCE> sourceCreator, ValueCreator<DEST> destCreator) {
    List<Mapper<SOURCE, DEST>> mappers = new ArrayList<>();
    while (it.hasNext()) {
      String line = it.next();
      if (line.isBlank()) {
        break;
      }
      if (line.contains("map:")) {
        continue;
      }
      // destStart sourceStart range
      long[] mapperValues = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
      mappers.add(new Mapper<>(mapperValues[0], mapperValues[1], mapperValues[2], sourceCreator, destCreator));
    }
    mappers.add(new Mapper<>(-1, -1, Long.MAX_VALUE, sourceCreator, destCreator));
    return mappers;
  }

  static class Mapper<SOURCE extends Value, DEST extends Value> {
    private final ValueCreator<SOURCE> sourceCreator;
    private final ValueCreator<DEST> destCreator;
    private final long sourceStart;
    private final long sourceEnd;
    private final long destStart;
    private final long destEnd;

    Mapper(long destStart, long sourceStart, long range, ValueCreator<SOURCE> sourceCreator, ValueCreator<DEST> destCreator) {
      this.sourceCreator = sourceCreator;
      this.sourceStart = sourceStart;
      this.sourceEnd = sourceStart + range; // exclusive
      this.destCreator = destCreator;
      this.destStart = destStart;
      this.destEnd = destStart + range; // exclusive
    }

    public Result<SOURCE, DEST> map(SOURCE source) {
      if (sourceStart == -1) {
        return new Result<>(List.of(), destCreator.apply(source.start(), source.end()));
      }

      boolean startInRange = source.start() >= sourceStart && source.start() < sourceEnd;
      boolean endInRange = source.end() >= sourceStart && source.end() < sourceEnd;
      boolean around = source.start() < sourceStart && source.end() >= sourceEnd;
      if (startInRange && endInRange) {
        DEST newDest = destCreator.apply(source.start() - sourceStart + destStart, source.end() - sourceStart + destStart);
        return new Result<>(List.of(), newDest);
      }
      else if (startInRange) {
        DEST newDest = destCreator.apply(source.start() - sourceStart + destStart, destEnd);
        SOURCE newSource = sourceCreator.apply(sourceEnd, source.end());
        return new Result<>(List.of(newSource), newDest);
      }
      else if (endInRange) {
        DEST newDest = destCreator.apply(destStart, source.end() - sourceStart + destStart);
        SOURCE newSource = sourceCreator.apply(source.start(), sourceStart);
        return new Result<>(List.of(newSource), newDest);
      }
      else if (around) {
        DEST newDest = destCreator.apply(destStart, destEnd);
        SOURCE newSource1 = sourceCreator.apply(source.start(), sourceStart);
        SOURCE newSource2 = sourceCreator.apply(sourceEnd, source.end());
        return new Result<>(List.of(newSource1, newSource2), newDest);
      }
      else {
        return new Result<>(List.of(source), null);
      }
    }

    record Result<SOURCE, DEST>(List<SOURCE> sources, DEST destination) { }
  }

  interface ValueCreator<V extends Value> {
    V apply(long start, long end);
  }

  sealed interface Value
      permits Seed, Soil, Fertilizer, Water, Light, Temperature, Humidity, Location {
    long start();
    long end();
  }

  record Seed(long start, long end) implements Value {  }
  record Soil(long start, long end) implements Value {  }
  record Fertilizer(long start, long end) implements Value {  }
  record Water(long start, long end) implements Value {  }
  record Light(long start, long end) implements Value {  }
  record Temperature(long start, long end) implements Value {  }
  record Humidity(long start, long end) implements Value {  }
  record Location(long start, long end) implements Value {  }

}
