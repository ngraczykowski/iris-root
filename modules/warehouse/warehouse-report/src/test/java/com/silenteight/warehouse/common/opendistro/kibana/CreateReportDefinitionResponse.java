package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
class CreateReportDefinitionResponse {

  @JsonProperty("scheduler_response")
  private SchedulerResponse schedulerResponse;

  @Data
  static class SchedulerResponse {

    private String reportDefinitionId;
  }
}
