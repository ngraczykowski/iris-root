package com.silenteight.sens.webapp.common.testing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class BasicInMemoryRepository<T> {

  private final ConcurrentMap<Long, T> store = new ConcurrentHashMap<>();

  protected final Map<Long, T> getInternalStore() {
    return store;
  }

  protected final Stream<T> stream() {
    return store.values().stream();
  }
}
