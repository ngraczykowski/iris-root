package com.silenteight.warehouse.report.reporting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class ReportingService {

  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final OpendistroKibanaClient opendistroKibanaClient;
  @NonNull
  private final TimeSource timeSource;

  public Set<String> getReportIds(String tenant) {
    ListReportsInstancesRequest listReportsInstancesRequest = ListReportsInstancesRequest.builder()
        .tenant(tenant)
        .build();

    return opendistroElasticClient.getReportInstances(listReportsInstancesRequest)
        .getReportInstancesList()
        .stream()
        .map(ReportInstance::getId)
        .collect(toSet());
  }

  public List<KibanaReportDefinitionDto> listReportDefinitions(String tenantName) {
    return opendistroKibanaClient.listReportDefinitions(tenantName);
  }

  public KibanaReportDto getReport(String tenant, String reportInstanceId) {
    return opendistroKibanaClient
        .getReportContent(tenant, reportInstanceId);
  }

  public ReportInstanceReferenceDto createReport(String reportDefinitionId, String tenant) {
    long timestamp = timeSource.now().toEpochMilli();
    opendistroKibanaClient.createReportInstanceAsync(tenant, reportDefinitionId);

    return new ReportInstanceReferenceDto(timestamp);
  }
}
