package com.silenteight.warehouse.indexer.alert;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

class SkipAnyMatchingKeysStrategy implements Predicate<String> {

  private final List<Pattern> predicates;

  SkipAnyMatchingKeysStrategy(List<String> ignoredKeysRegExp) {
    predicates = ignoredKeysRegExp.stream()
        .map(Pattern::compile)
        .collect(toList());
  }

  @Override
  public boolean test(String value) {
    return predicates.stream()
        .noneMatch(pattern -> pattern.matcher(value).matches());
  }
}
