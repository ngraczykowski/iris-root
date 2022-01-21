package com.silenteight.agent.common.dictionary;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Builder
class DictionaryInstanceProvider<TargetT extends Dictionary> {

  private static final Map<DictionaryInstanceKey, Dictionary> INSTANCES = new ConcurrentHashMap<>();

  private final String identifier;
  private final Class<TargetT> clazz;
  private final Supplier<TargetT> factory;

  TargetT getOrCreate() {
    var key = new DictionaryInstanceKey(identifier, clazz);
    var dictionary = INSTANCES.computeIfAbsent(key, k -> createDictionary());
    return tryCastDictionary(dictionary)
        .orElseThrow(() -> new InvalidDictionaryClassException(key));
  }

  private TargetT createDictionary() {
    var targetT = factory.get();
    log.info("{} loaded successfully from {}.", clazz.getSimpleName(), identifier);
    return targetT;
  }

  private Optional<TargetT> tryCastDictionary(Dictionary dictionary) {
    return Optional.of(dictionary)
        .filter(d -> clazz.isAssignableFrom(d.getClass()))
        .map(clazz::cast);
  }

  static class InvalidDictionaryClassException extends IllegalStateException {

    private static final long serialVersionUID = -7768622177493127672L;

    InvalidDictionaryClassException(DictionaryInstanceKey key) {
      super("Could not resolve dictionary for" + key);
    }
  }

  @Value
  private static class DictionaryInstanceKey {

    String identifier;
    Class<? extends Dictionary> clazz;
  }
}
