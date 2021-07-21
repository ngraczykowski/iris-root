package com.silenteight.warehouse.report.rbs.list;

import com.silenteight.warehouse.report.rbs.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.SimulationReportsProvider;

import java.util.List;

public class RbsSimulationReportProvider implements SimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return ReportDefinition.toReportsDefinitionDto(analysisId);
  }
}
