package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

@Slf4j
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
    ReportInstanceReferenceDto report = reportingService.createReport(definitionId, tenantName);

    log.debug("Simulation report scheduled for generation, "
            + "analysiId={}, definitionId={}, tenantName={}, reference={}",
        analysisId, definitionId, tenantName, report);

    return report;
  }

  public KibanaReportDto downloadReport(
      String analysisId, String reportDefinitionId, Long timestamp) {

    String tenantName = simulationReportingQuery.getTenantIdByAnalysisId(analysisId);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }
}
