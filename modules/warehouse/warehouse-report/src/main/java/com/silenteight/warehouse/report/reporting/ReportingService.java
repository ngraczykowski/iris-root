package com.silenteight.warehouse.report.reporting;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstanceList;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ReportingService {

  private final OpendistroElasticClient opendistroElasticClient;
  private final OpendistroKibanaClient opendistroKibanaClient;

  public List<String> getReportList(String tenant) {
    ListReportsInstancesRequest listReportsInstancesRequest = ListReportsInstancesRequest.builder()
        .tenant(tenant)
        .build();

    return opendistroElasticClient.getReportInstance(listReportsInstancesRequest)
        .getReportInstanceList()
        .stream()
        .map(ReportInstanceList::getId)
        .collect(toList());
  }

  public String getReportContent(String tenant, String reportInstanceId) {
    return opendistroKibanaClient.getReportContent(tenant, reportInstanceId);
  }
}
