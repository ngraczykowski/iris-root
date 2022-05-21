package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RequiredArgsConstructor
enum Splitter {
  COMMA("\\s*,\\s*"),
  SEMICOLON("\\s*;\\s*"),
  SPACE("\\s* \\s*");

  private final String pattern;

  SplittingResult split(String country) {
    return new SplittingResult(List.of(country.split(pattern)));
  }

  @RequiredArgsConstructor
  static class SplittingResult {

    private final List<String> values;

    boolean allMatch(Function<String, String> preFilteringMapper, Predicate<String> predicate) {
      return values.stream()
          .map(preFilteringMapper)
          .map(String::strip)
          .filter(StringUtils::isNotEmpty)
          .allMatch(predicate);
    }

    Stream<String> map(Function<String, String> mapper) {
      return values.stream()
          .map(mapper)
          .filter(StringUtils::isNotEmpty);
    }
  }

}
