package com.silenteight.warehouse.report.metrics.v1.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportService;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.SIMULATION;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.getReportType;

@RequiredArgsConstructor
public class DeprecatedCreateMetricsReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final DeprecatedMetricsReportService reportService;

  ReportInstanceReferenceDto createSimulationReport(String analysisId) {
    return reportService.createSimulationReportInstance(SIMULATION, analysisId);
  }

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    return reportService.createProductionReportInstance(
        getReportType(reportId), PRODUCTION_ANALYSIS_NAME);
  }
}
