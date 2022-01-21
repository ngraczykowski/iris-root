package com.silenteight.agent.common.dictionary;

import lombok.Getter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.TRIM_AND_UPPER_CASE;
import static java.util.stream.Collectors.toSet;

public class UniqueValuesDictionary implements Dictionary {

  /**
   * Line Format: <pre>value</pre>
   *
   * <p>Returned values are UPPER CASE. Ignores # comments or empty lines.
   */
  public static UniqueValuesDictionary fromSource(DictionarySource source) {
    return source.getOrCreate(UniqueValuesDictionary.class, UniqueValuesDictionary::new);
  }

  @Getter
  private final Set<String> allValues;

  private UniqueValuesDictionary(Stream<String> stream) {
    allValues = stream
        .filter(DictionaryLineFilters.IGNORE_EMPTY_LINES)
        .filter(DictionaryLineFilters.IGNORE_COMMENTS)
        .map(TRIM_AND_UPPER_CASE)
        .collect(toSet());
  }

  public boolean contains(String value) {
    return allValues.contains(value.toUpperCase());
  }

  public boolean containsAll(Collection<String> values) {
    values = values.stream().map(String::toUpperCase).collect(Collectors.toUnmodifiableList());
    return allValues.containsAll(values);
  }
}
