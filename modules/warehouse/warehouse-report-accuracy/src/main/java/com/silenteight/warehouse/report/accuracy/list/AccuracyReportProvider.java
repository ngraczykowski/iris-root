package com.silenteight.warehouse.report.accuracy.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.SimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.toSimulationReportsDefinitionDto;

class AccuracyReportProvider implements SimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toSimulationReportsDefinitionDto(analysisId);
  }
}
