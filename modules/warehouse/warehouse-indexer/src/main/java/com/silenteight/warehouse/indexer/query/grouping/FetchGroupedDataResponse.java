package com.silenteight.warehouse.indexer.query.grouping;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Value
@AllArgsConstructor
@Builder
public class FetchGroupedDataResponse {

  static FetchGroupedDataResponse empty() {
    return FetchGroupedDataResponse.builder()
        .rows(emptyList())
        .build();
  }

  @NonNull
  @Builder.Default
  List<Row> rows = List.of();

  @RequiredArgsConstructor
  @Value
  @Builder
  public static class Row {

    long count;
    @Getter(AccessLevel.PRIVATE)
    Map<String, String> data;

    public String getValue(String fieldName) {
      return data.get(fieldName);
    }

    public String getValueOrDefault(String fieldName, String defaultValue) {
      return Optional.ofNullable(data.get(fieldName)).orElse(defaultValue);
    }

    public long getCount() {
      return count;
    }
  }
}
