package com.silenteight.warehouse.indexer.query.streaming;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

@Value
public class FetchedDocument {

  @NonNull
  Set<Fields> fields;

  public FetchedDocument(Map<String, Object> map) {
    fields = map.entrySet()
       .stream()
       .map(entry -> new Fields(entry.getKey(), entry.getValue().toString()))
       .collect(Collectors.toUnmodifiableSet());
  }

  public String getFieldValue(String fieldName) {
    return fields
        .stream()
        .filter(field -> field.hasName(fieldName))
        .map(Fields::getFieldValue)
        .findAny()
        .orElse("");
  }

  @Value
  private static class Fields {

    @NonNull
    String fieldName;
    @Nullable
    String fieldValue;

    boolean hasName(@NonNull String fieldName) {
      return fieldName.equals(this.fieldName);
    }
  }
}
