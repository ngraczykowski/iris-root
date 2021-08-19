package com.silenteight.warehouse.report.metrics.list;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.SimulationReportsProvider;

import java.util.List;

class MetricsReportProvider implements SimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return MetricsReportDefinition.toReportsDefinitionDto(analysisId);
  }
}
