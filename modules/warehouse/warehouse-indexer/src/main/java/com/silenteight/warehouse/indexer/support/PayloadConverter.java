package com.silenteight.warehouse.indexer.support;

import lombok.NonNull;

import com.google.protobuf.Struct;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

public class PayloadConverter {

  @NonNull
  private final Predicate<String> allowedKeysPredicate;

  public PayloadConverter(@NonNull List<String> ignoredKeys) {
    allowedKeysPredicate = new SkipAnyMatchingKeysStrategy(ignoredKeys);
  }

  public Map<String, String> convertPayloadToMap(Struct struct, String prefix) {
    return struct
        .getFieldsMap()
        .keySet()
        .stream()
        .filter(allowedKeysPredicate)
        .collect(toMap(
            key -> prefix + key,
            key -> struct.getFieldsMap().get(key).getStringValue()));
  }
}
