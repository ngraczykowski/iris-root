package com.silenteight.warehouse.report.sm.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.SimulationReportsProvider;
import com.silenteight.warehouse.report.sm.domain.ReportDefinition;

import java.util.List;

class SimulationMetricsReportProvider implements SimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return ReportDefinition.toReportsDefinitionDto(analysisId);
  }
}
