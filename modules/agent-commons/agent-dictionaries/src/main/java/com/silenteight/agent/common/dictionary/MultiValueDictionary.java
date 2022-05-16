package com.silenteight.agent.common.dictionary;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.INVERTED_KEYS_AND_VALUES;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.KEYS_AND_VALUES;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.TRIM_AND_UPPER_CASE;
import static com.silenteight.agents.logging.AgentLogger.trace;

@Slf4j
public class MultiValueDictionary implements Dictionary {

  /**
   * Line Format: <pre>value1;value2=key1;key2</pre>
   *
   * <p>Keys are case-insensitive. Returned values are UPPER CASE. Ignores # comments or empty
   * lines.
   */
  public static MultiValueDictionary fromSourceInverted(DictionarySource source) {
    return source.getOrCreate(
        MultiValueDictionary.class, stream -> new MultiValueDictionary(stream, true));
  }

  /**
   * Line Format: <pre>key1;key2=value1;value2</pre>
   *
   * <p>Keys are case-insensitive. Returned values are UPPER CASE. Ignores # comments or empty
   * lines.
   */
  public static MultiValueDictionary fromSource(DictionarySource source) {
    return source.getOrCreate(
        MultiValueDictionary.class, stream -> new MultiValueDictionary(stream, false));
  }

  private final Map<String, List<String>> map = new HashMap<>();

  private MultiValueDictionary(Stream<String> stream, boolean inverted) {
    stream
        .filter(DictionaryLineFilters.IGNORE_EMPTY_LINES)
        .filter(DictionaryLineFilters.IGNORE_COMMENTS)
        .map(TRIM_AND_UPPER_CASE)
        .map(inverted ? INVERTED_KEYS_AND_VALUES : KEYS_AND_VALUES)
        .forEach(this::consumeEntry);
  }

  private void consumeEntry(KeysAndMultipleValues entry) {
    var keys = entry.getKeys();
    var values = entry.getValues();
    keys.forEach(key -> map.merge(key, values, MultiValueDictionary::mergeValues));
  }

  private static List<String> mergeValues(List<String> values1, List<String> values2) {
    return Stream.concat(values1.stream(), values2.stream())
        .distinct()
        .collect(Collectors.toUnmodifiableList());
  }

  public List<String> findByKey(String key) {
    var result = Optional.ofNullable(map.get(key.toUpperCase()));

    result.ifPresentOrElse(
        values -> trace(log, "Values for {} found: {}", () -> key, () -> values),
        () -> trace(log, "Values for {} not found", () -> key)
    );

    return result.orElse(List.of());
  }

  public boolean hasMappingFor(String first, String second) {
    return hasValueForKey(first, second) || hasValueForKey(second, first);
  }

  private boolean hasValueForKey(String key, String value) {
    return findByKey(key).contains(value.toUpperCase());
  }
}
