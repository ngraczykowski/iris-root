package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

@RequiredArgsConstructor
public class SimulationService {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final SimulationReportingQuery simulationReportingQuery;

  @NonNull
  private final UserAwareReportingService userAwareReportingService;

  public ReportInstanceReferenceDto createSimulationReport(String analysisId, String definitionId) {
    String tenantName = simulationReportingQuery.getTenantIdByAnalysisId(analysisId);
    return reportingService.createReport(definitionId, tenantName);
  }

  public KibanaReportDto downloadReport(
      String analysisId, String reportDefinitionId, Long timestamp) {

    String tenantName = simulationReportingQuery.getTenantIdByAnalysisId(analysisId);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }
}
