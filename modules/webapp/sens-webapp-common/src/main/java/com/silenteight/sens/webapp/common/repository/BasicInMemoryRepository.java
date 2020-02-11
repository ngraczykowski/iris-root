package com.silenteight.sens.webapp.common.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class BasicInMemoryRepository<K, T> {

  private final ConcurrentMap<K, T> store = new ConcurrentHashMap<>();

  protected final Map<K, T> getInternalStore() {
    return store;
  }

  protected final Stream<T> stream() {
    return store.values().stream();
  }
}
