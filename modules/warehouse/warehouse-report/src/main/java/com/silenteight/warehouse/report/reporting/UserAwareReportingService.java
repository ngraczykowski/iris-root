package com.silenteight.warehouse.report.reporting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import java.util.Optional;

import static java.util.Comparator.comparingLong;

@Slf4j
@RequiredArgsConstructor
public class UserAwareReportingService {

  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final OpendistroKibanaClient opendistroKibanaClient;

  public KibanaReportDto downloadReport(
      String tenantName, String reportDefinitionId, Long timestamp) {

    String reportInstanceId = getReportInstanceId(tenantName, reportDefinitionId, timestamp)
        .orElseThrow(
            () -> ReportInstanceNotFoundException.of(tenantName, reportDefinitionId, timestamp));

    return opendistroKibanaClient.getReportContent(tenantName, reportInstanceId);
  }

  public Optional<String> getReportInstanceId(
      String tenantName, String reportDefinitionId, Long timestamp) {

    ListReportsInstancesRequest request = ListReportsInstancesRequest.builder()
        .tenant(tenantName)
        .build();

    return opendistroElasticClient
        .getReportInstances(request)
        .getReportInstancesList()
        .stream()
        .filter(instance -> instance.hasReportDefinitionId(reportDefinitionId))
        .filter(instance -> instance.isCreatedAfter(timestamp))
        .min(comparingLong(ReportInstance::getCreatedTimeMs))
        .map(ReportInstance::getId);
  }
}
