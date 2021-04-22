package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListReportsInstancesResponse {

  Long totalHits;
  List<ReportInstanceList> reportInstanceList = new ArrayList<>();

  @Data
  public static class ReportInstanceList {
    String id;
    String status;
  }
}
