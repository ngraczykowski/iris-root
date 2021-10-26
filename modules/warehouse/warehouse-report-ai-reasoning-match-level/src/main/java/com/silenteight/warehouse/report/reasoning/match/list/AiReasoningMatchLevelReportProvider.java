package com.silenteight.warehouse.report.reasoning.match.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.toSimulationReportsDefinitionDto;

class AiReasoningMatchLevelReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
