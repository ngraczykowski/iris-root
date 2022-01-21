package com.silenteight.agent.common.dictionary;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.MULTIPLE_VALUES;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.TRIM_AND_UPPER_CASE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
public class RelatedValuesDictionary implements Dictionary {

  /**
   * Line Format: <pre>value1;value2;value3</pre>
   *
   * <p>Keys are case-insensitive. Returned values are UPPER CASE. Ignores # comments or empty
   * lines.
   */
  public static RelatedValuesDictionary fromSource(DictionarySource source) {
    return source.getOrCreate(RelatedValuesDictionary.class, RelatedValuesDictionary::new);
  }

  private final Map<String, List<String>> map = new HashMap<>();

  private RelatedValuesDictionary(Stream<String> stream) {
    stream
        .filter(DictionaryLineFilters.IGNORE_EMPTY_LINES)
        .filter(DictionaryLineFilters.IGNORE_COMMENTS)
        .map(TRIM_AND_UPPER_CASE)
        .map(MULTIPLE_VALUES)
        .forEach(relatedNames -> relatedNames.getValues().forEach(name ->
            map.merge(name.toUpperCase(), filterValuesSameAsKey(relatedNames.getValues(), name),
                RelatedValuesDictionary::mergeCollectionsToList)
        ));
  }

  private static List<String> filterValuesSameAsKey(List<String> values, String key) {
    return values.stream()
        .filter(value -> !value.equalsIgnoreCase(key)).collect(toList());
  }

  private static List<String> mergeCollectionsToList(
      Collection<String> collection1, Collection<String> collection2) {
    return Stream
        .of(collection1, collection2)
        .flatMap(Collection::stream)
        .distinct()
        .collect(toList());
  }

  public List<String> findByValue(String value) {
    var result = Optional.ofNullable(map.get(value.toUpperCase()));
    if (log.isTraceEnabled()) {
      result.ifPresentOrElse(
          values -> log.trace("Values for {} found: {}", value, values),
          () -> log.trace("Values for {} not found", value)
      );
    }
    return result.orElse(emptyList());
  }

  public boolean hasValue(String value) {
    return map.containsKey(value.toUpperCase());
  }
}
