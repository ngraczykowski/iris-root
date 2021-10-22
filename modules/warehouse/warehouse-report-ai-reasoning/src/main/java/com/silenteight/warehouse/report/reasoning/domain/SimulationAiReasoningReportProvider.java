package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.simulation.SimulationReportProvider;

import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReport.toSimulationReportTypeDto;

class SimulationAiReasoningReportProvider implements SimulationReportProvider {

  @Override
  public ReportTypeDto getReport(String analysisId) {
    return toSimulationReportTypeDto(analysisId);
  }
}
