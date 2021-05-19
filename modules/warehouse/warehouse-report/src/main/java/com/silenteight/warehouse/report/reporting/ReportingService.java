package com.silenteight.warehouse.report.reporting;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesRequest;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import java.util.Set;

import static com.silenteight.warehouse.report.reporting.AnalysisResource.toResourceName;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class ReportingService {

  private final OpendistroElasticClient opendistroElasticClient;
  private final OpendistroKibanaClient opendistroKibanaClient;
  private final AnalysisService analysisService;

  public KibanaReportsList getReportDtoList(String analysisName) {
    ListReportsInstancesRequest listReportsInstancesRequest = ListReportsInstancesRequest.builder()
        .tenant(getTenantIdByAnalysisId(analysisName))
        .build();

    return KibanaReportsList.builder()
        .reports(getReports(listReportsInstancesRequest))
        .build();
  }

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

  public KibanaReportDto getReport(String tenant, String reportInstanceId) {
    return opendistroKibanaClient.getReportContent(tenant, reportInstanceId);
  }

  public TenantNameWrapper getTenantNameWrapperByAnalysis(String analysisName) {
    String tenantIdByAnalysisId = getTenantIdByAnalysisId(analysisName);
    return TenantNameWrapper.builder()
        .tenantName(tenantIdByAnalysisId)
        .build();
  }

  private String getTenantIdByAnalysisId(String analysisName) {
    return analysisService.getTenantIdByAnalysis(toResourceName(analysisName));
  }

  private Set<KibanaReportDetailsDto> getReports(
      ListReportsInstancesRequest listReportsInstancesRequest) {
    return opendistroElasticClient
        .getReportInstances(listReportsInstancesRequest)
        .getReportInstancesList()
        .stream()
        .map(this::convertToDto)
        .collect(toSet());
  }

  public KibanaReportDetailsDto convertToDto(ReportInstance reportInstance) {
    return KibanaReportDetailsDto.builder()
        .id(reportInstance.getId())
        .title(reportInstance.getReportDefinitionDetails().getReportDefinition().getName())
        .build();
  }
}
