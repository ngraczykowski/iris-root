package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import static com.silenteight.warehouse.report.reporting.ReportStatus.buildReportStatusGenerating;
import static com.silenteight.warehouse.report.reporting.ReportStatus.buildReportStatusOk;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.ANALYSIS_ID_PARAM;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.DEFINITION_ID_PARAM;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.REPORTS_RESOURCE_NAME;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.TIMESTAMP_PARAM;
import static java.util.Map.of;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Slf4j
@RequiredArgsConstructor
class SimulationService {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final SimulationReportingQuery simulationReportingQuery;

  @NonNull
  private final UserAwareReportingService userAwareReportingService;

  public ReportInstanceReferenceDto createSimulationReport(String analysisId, String definitionId) {
    String tenantName = getTenantName(analysisId);
    ReportInstanceReferenceDto report = reportingService.createReport(definitionId, tenantName);

    log.debug("Simulation report scheduled for generation, "
            + "analysiId={}, definitionId={}, tenantName={}, reference={}",
        analysisId, definitionId, tenantName, report);

    return report;
  }

  public KibanaReportDto downloadReport(
      String analysisId, String reportDefinitionId, Long timestamp) {

    String tenantName = getTenantName(analysisId);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }

  public ReportStatus getReportGeneratingStatus(
      String analysisId, String definitionId, Long timestamp) {

    String tenantName = getTenantName(analysisId);
    String reportName = buildReportName(analysisId, definitionId, timestamp);

    return userAwareReportingService.getReportInstanceId(tenantName, definitionId, timestamp)
        .map(id -> buildReportStatusOk(reportName))
        .orElse(buildReportStatusGenerating(reportName));
  }

  private String getTenantName(String analysisId) {
    return simulationReportingQuery.getTenantIdByAnalysisId(analysisId);
  }

  private static String buildReportName(
      String analysisId, String definitionId, Long timestamp) {

    return fromPath(REPORTS_RESOURCE_NAME)
        .buildAndExpand(of(ANALYSIS_ID_PARAM, analysisId,
            DEFINITION_ID_PARAM, definitionId,
            TIMESTAMP_PARAM, timestamp))
        .toUriString();
  }
}
