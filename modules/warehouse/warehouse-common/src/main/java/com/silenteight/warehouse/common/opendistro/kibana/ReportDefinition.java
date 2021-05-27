package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReportDefinition {

  @JsonProperty("_id")
  @Nullable
  String id;

  @JsonProperty("_source")
  @Nullable
  ReportDefinitionDetailsWrapper details;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class ReportDefinitionDetailsWrapper {

    @JsonProperty("report_definition")
    @Nullable
    ReportDefinitionDetails reportDefinition;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  static class ReportDefinitionDetails {

    @JsonProperty("report_params")
    ReportParams reportParams;
    JsonNode trigger;
    JsonNode delivery;
    @JsonProperty("time_created")
    Long timeCreated;
    @JsonProperty("last_updated")
    Long lastUpdated;
    String status;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  static class ReportParams {

    @JsonProperty("report_name")
    String reportName;
    @JsonProperty("report_source")
    String reportSource;
    @JsonInclude
    String description;
    @JsonProperty("core_params")
    CoreParams coreParams;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  static class CoreParams {

    @JsonProperty("base_url")
    String baseUrl;
    @JsonProperty("report_format")
    String reportFormat;
    @JsonProperty("time_duration")
    String timeDuration;
    @JsonProperty("saved_search_id")
    String savedSearchId;
    @JsonInclude(Include.NON_NULL)
    String origin;
    Integer limit;
    Boolean excel;
  }

  KibanaReportDefinitionDto toDto() {
    return KibanaReportDefinitionDto.builder()
        .id(getId())
        .reportDefinitionDetails(getDetails().getReportDefinition())
        .build();
  }
}
