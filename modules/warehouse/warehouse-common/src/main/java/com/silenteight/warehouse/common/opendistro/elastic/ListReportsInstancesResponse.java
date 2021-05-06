package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListReportsInstancesResponse {

  @JsonProperty("totalHits")
  Long totalHits;
  @JsonProperty("reportInstanceList")
  List<ReportInstanceList> reportInstanceList = new ArrayList<>();

  @Data
  public static class ReportInstanceList {
    String id;
    String status;
  }
}
