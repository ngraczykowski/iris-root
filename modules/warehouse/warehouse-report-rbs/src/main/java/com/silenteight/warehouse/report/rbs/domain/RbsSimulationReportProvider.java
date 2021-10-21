package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.simulation.SimulationReportProvider;

import static com.silenteight.warehouse.report.rbs.domain.RbsReport.toSimulationReportTypeDto;

class RbsSimulationReportProvider implements SimulationReportProvider {

  @Override
  public ReportTypeDto getReport(String analysisId) {
    return toSimulationReportTypeDto(analysisId);
  }
}
