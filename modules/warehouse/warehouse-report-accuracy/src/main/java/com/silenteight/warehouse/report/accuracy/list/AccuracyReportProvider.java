package com.silenteight.warehouse.report.accuracy.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.toSimulationReportsDefinitionDto;

class AccuracyReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
