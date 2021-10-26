package com.silenteight.warehouse.report.accuracy.v1.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportDefinition.toSimulationReportsDefinitionDto;

class DeprecatedAccuracyReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
