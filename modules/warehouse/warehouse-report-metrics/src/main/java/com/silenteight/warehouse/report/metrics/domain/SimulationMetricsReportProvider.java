package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.simulation.SimulationReportProvider;

import static com.silenteight.warehouse.report.metrics.domain.MetricsReport.toSimulationReportTypeDto;

class SimulationMetricsReportProvider implements SimulationReportProvider {

  @Override
  public ReportTypeDto getReport(String analysisId) {
    return toSimulationReportTypeDto(analysisId);
  }
}
