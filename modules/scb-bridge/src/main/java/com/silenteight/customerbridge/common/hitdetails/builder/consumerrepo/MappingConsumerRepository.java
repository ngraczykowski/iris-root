package com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MappingConsumerRepository<A, B> implements ConsumerRepository<A, B> {

  private final Map<String, BiConsumer<A, B>> map = new HashMap<>();

  void register(String key, BiConsumer<A, B> consumer) {
    map.put(key, (h, v) -> Optional.ofNullable(v)
        .ifPresent(c -> consumer.accept(h, c)));
  }

  <T> void register(String key, Function<B, T> mapper, BiConsumer<A, T> consumer) {
    Function<B, T> mappingFunction = createMappingFunction(mapper);
    map.put(key, (h, v) -> Optional.ofNullable(v)
        .map(mappingFunction)
        .ifPresent(c -> consumer.accept(h, c)));
  }

  private <T> Function<B, T> createMappingFunction(Function<B, T> function) {
    return o -> map(function, o);
  }

  // NOTE(bgulowaty): Suppressing squid:S1181 because it is expected to catch Errors here.
  private <T> T map(Function<B, T> function, B o) {
    try {
      return function.apply(o);
    } catch (@SuppressWarnings("squid:S1181") Throwable e) {
      throw new MappingValueException(o, e);
    }
  }

  @Override
  public Optional<BiConsumer<A, B>> find(@NonNull String key) {
    return Optional.ofNullable(map.get(key));
  }

  static class MappingValueException extends RuntimeException {

    private static final long serialVersionUID = -4318403042676877788L;

    private static final String MESSAGE = "Cannot map object: %s";

    MappingValueException(@NonNull Object o, Throwable e) {
      super(String.format(MESSAGE, o), e);
    }
  }
}
