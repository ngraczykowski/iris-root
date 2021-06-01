package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.*;
import lombok.Builder.Default;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListReportsInstancesResponse {

  @JsonProperty("totalHits")
  Long totalHits;
  @JsonProperty("reportInstanceList")
  @Default
  List<ReportInstance> reportInstancesList = new ArrayList<>();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ReportInstance {

    @NonNull
    private String id;
    @NonNull
    private String status;
    @NonNull
    private Long createdTimeMs;
    @NonNull
    private ReportDefinitionDetails reportDefinitionDetails;

    public boolean hasReportDefinitionId(String id) {
      return getReportDefinitionDetails().getId().equals(id);
    }

    public boolean isCreatedAfter(Long timestamp) {
      return getCreatedTimeMs() > timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReportDefinition {

      @NonNull
      private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReportDefinitionDetails {

      @NonNull
      private String id;

      @NonNull
      private ReportDefinition reportDefinition;
    }
  }
}
