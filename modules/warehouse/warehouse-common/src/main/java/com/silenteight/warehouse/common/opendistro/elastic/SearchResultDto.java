package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.*;
import lombok.Builder.Default;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import static java.util.List.of;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResultDto {

  @NonNull
  private Aggregation aggregations;

  public List<Bucket> getBuckets() {
    return aggregations.getCompositeBuckets().getBuckets();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class Aggregation {

    @NonNull
    @JsonProperty("composite_buckets")
    private CompositeBuckets compositeBuckets;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static class CompositeBuckets {

    @NonNull
    @Default
    private List<Bucket> buckets = of();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Bucket {

    @NonNull
    @Default
    private Map<String, String> key = Map.of();

    @JsonProperty("doc_count")
    private long docCount;
  }
}
