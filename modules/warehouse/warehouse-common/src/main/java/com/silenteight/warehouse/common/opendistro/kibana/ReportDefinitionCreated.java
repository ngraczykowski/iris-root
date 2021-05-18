package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReportDefinitionCreated {

  String state;

  @JsonProperty("scheduler_response")
  SchedulerResponse schedulerResponse;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class SchedulerResponse {

    @JsonProperty("reportDefinitionId")
    String reportDefinitionId;
  }
}
