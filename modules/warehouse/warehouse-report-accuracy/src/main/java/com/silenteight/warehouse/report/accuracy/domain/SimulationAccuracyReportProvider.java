package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.simulation.SimulationReportProvider;

import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReport.toSimulationReportTypeDto;

class SimulationAccuracyReportProvider implements SimulationReportProvider {

  @Override
  public ReportTypeDto getReport(String analysisId) {
    return toSimulationReportTypeDto(analysisId);
  }
}
