package com.silenteight.agent.common.dictionary;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class BaseNormalizedValuesDictionary implements Dictionary {

  private final Set<String> allValues;
  private final UnaryOperator<String> normalizer;

  protected BaseNormalizedValuesDictionary(
      Stream<String> stream, UnaryOperator<String> normalizer) {
    allValues = stream
        .filter(DictionaryLineFilters.IGNORE_EMPTY_LINES)
        .filter(DictionaryLineFilters.IGNORE_COMMENTS)
        .map(DictionaryLineTransformers.TRIM_AND_UPPER_CASE)
        .map(normalizer)
        .collect(Collectors.toSet());
    this.normalizer = normalizer;
  }

  public boolean contains(String input) {
    return allValues.contains(normalizer.apply(input));
  }
}
