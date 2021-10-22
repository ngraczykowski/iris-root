package com.silenteight.warehouse.report.reasoning.v1.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportDefinition.toSimulationReportsDefinitionDto;

class DeprecatedAiReasoningReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
