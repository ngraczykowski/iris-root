package com.silenteight.warehouse.common.opendistro.elastic;


import lombok.*;
import lombok.Builder.Default;

import java.util.List;
import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryResultDto {

  @Default
  @NonNull
  List<SchemaEntry> schema = List.of();
  @ToString.Exclude
  @Default
  @NonNull
  List<List<Object>> datarows = List.of();
  @Nullable
  Integer total;
  @Nullable
  Integer size;
  @Nullable
  Integer status;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SchemaEntry {

    @NonNull
    String name;
  }
}
