package com.silenteight.agent.common.dictionary;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.agents.logging.AgentLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.silenteight.agent.common.dictionary.DictionaryLineFilters.IGNORE_COMMENTS;
import static com.silenteight.agent.common.dictionary.DictionaryLineFilters.IGNORE_EMPTY_LINES;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.INVERTED_KEYS_AND_VALUE;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.KEYS_AND_VALUE;
import static com.silenteight.agent.common.dictionary.DictionaryLineTransformers.TRIM_AND_UPPER_CASE;
import static java.util.Optional.ofNullable;

@Slf4j
public class SingleValueDictionary implements Dictionary {


  /**
   * Line Format: <pre>value1;value2=key</pre>
   *
   * <p>Keys are case-insensitive. Returned value is UPPER CASE. Ignores # comments or empty lines.
   */
  public static SingleValueDictionary fromSourceInverted(DictionarySource source) {
    return source.getOrCreate(
        SingleValueDictionary.class, stream -> new SingleValueDictionary(stream, true));
  }

  /**
   * Line Format: <pre>key=value1;value2</pre>
   *
   * <p>Keys are case-insensitive. Returned value is UPPER CASE. Ignores # comments or empty lines.
   */
  public static SingleValueDictionary fromSource(DictionarySource source) {
    return source.getOrCreate(
        SingleValueDictionary.class, stream -> new SingleValueDictionary(stream, false));
  }

  private final Map<String, String> map = new HashMap<>();

  private SingleValueDictionary(Stream<String> stream, boolean inverted) {
    stream
        .filter(IGNORE_EMPTY_LINES)
        .filter(IGNORE_COMMENTS)
        .map(TRIM_AND_UPPER_CASE)
        .map(inverted ? INVERTED_KEYS_AND_VALUE : KEYS_AND_VALUE)
        .forEach(entry -> entry.getKeys().forEach(key -> map.put(key, entry.getValue())));
  }

  public Optional<String> findByKey(String key) {
    var result = ofNullable(map.get(key.toUpperCase()));

    result.ifPresentOrElse(
        value -> AgentLogger.trace(log, "Value for {} found: {}", () -> key, () -> value),
        () -> AgentLogger.trace(log, "Value for {} not found", () -> key)
    );

    return result;
  }

  public boolean hasMappingFor(String first, String second) {
    return hasValueForKey(first, second) || hasValueForKey(second, first);
  }

  private boolean hasValueForKey(String key, String value) {
    return findByKey(key.toUpperCase())
        .map(v -> v.equals(value.toUpperCase()))
        .orElse(Boolean.FALSE);
  }
}
