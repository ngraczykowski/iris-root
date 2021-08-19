package com.silenteight.warehouse.report.metrics.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.SIMULATION;
import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.getReportType;

@RequiredArgsConstructor
public class CreateMetricsReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final MetricsReportService reportService;

  ReportInstanceReferenceDto createSimulationReport(String analysisId) {
    return reportService.createSimulationReportInstance(SIMULATION, analysisId);
  }

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    return reportService.createProductionReportInstance(
        getReportType(reportId), PRODUCTION_ANALYSIS_NAME);
  }
}
