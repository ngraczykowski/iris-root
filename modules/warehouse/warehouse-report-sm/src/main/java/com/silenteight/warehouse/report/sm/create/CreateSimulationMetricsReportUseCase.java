package com.silenteight.warehouse.report.sm.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.sm.domain.ReportDefinition;
import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;

@RequiredArgsConstructor
public class CreateSimulationMetricsReportUseCase {

  @NonNull
  private final SimulationMetricsReportService reportService;

  ReportInstanceReferenceDto activate(String analysisId) {
    return reportService.createReportInstance(ReportDefinition.SIMULATION_METRICS, analysisId);
  }
}
