package com.silenteight.warehouse.report.reasoning.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.SimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportDefinition.toSimulationReportsDefinitionDto;

class AiReasoningReportProvider implements SimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
