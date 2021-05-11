package com.silenteight.warehouse.report.reporting;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstanceList;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class ReportingService {

  private final OpendistroElasticClient opendistroElasticClient;
  private final OpendistroKibanaClient opendistroKibanaClient;

  public Set<String> getReportList(String tenant) {
    ListReportsInstancesRequest listReportsInstancesRequest = ListReportsInstancesRequest.builder()
        .tenant(tenant)
        .build();

    return opendistroElasticClient.getReportInstance(listReportsInstancesRequest)
        .getReportInstanceList()
        .stream()
        .map(ReportInstanceList::getId)
        .collect(toSet());
  }

  public KibanaReportDto getReport(String tenant, String reportInstanceId) {
    return opendistroKibanaClient.getReportContent(tenant, reportInstanceId);
  }
}
