package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.*;

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
    private ReportDefinitionDetails reportDefinitionDetails;

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
      private ReportDefinition reportDefinition;
    }
  }
}
