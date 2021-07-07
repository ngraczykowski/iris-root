package com.silenteight.warehouse.indexer.query.grouping;

import lombok.*;

import java.util.List;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class FetchGroupedDataResponse {

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

    public long getCount() {
      return count;
    }
  }
}
