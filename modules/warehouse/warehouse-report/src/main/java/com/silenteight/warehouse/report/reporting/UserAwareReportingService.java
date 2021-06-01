package com.silenteight.warehouse.report.reporting;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import static com.silenteight.warehouse.report.reporting.AnalysisResource.toResourceName;
import static java.util.Comparator.comparingLong;

@Slf4j
@RequiredArgsConstructor
public class UserAwareReportingService {

  @NonNull
  private final AnalysisService analysisService;
  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;
  @NonNull
  private final OpendistroKibanaClient opendistroKibanaClient;

  public KibanaReportDto downloadReport(
      String reportDefinitionId, String analysisId, Long timestamp) {

    String tenantName = analysisService.getTenantIdByAnalysis(toResourceName(analysisId));

    ListReportsInstancesRequest request = ListReportsInstancesRequest.builder()
        .tenant(tenantName)
        .build();
    String reportInstanceId = opendistroElasticClient
        .getReportInstances(request)
        .getReportInstancesList()
        .stream()
        .filter(instance -> instance.hasReportDefinitionId(reportDefinitionId))
        .filter(instance -> instance.isCreatedAfter(timestamp))
        .min(comparingLong(ReportInstance::getCreatedTimeMs))
        .orElseThrow(ReportInstanceNotFoundException::new)
        .getId();

    return opendistroKibanaClient.getReportContent(tenantName, reportInstanceId);
  }
}
