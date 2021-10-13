package com.silenteight.warehouse.report.metrics.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportDefinition.toReportsDefinitionDto;

class DeprecatedMetricsReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toReportsDefinitionDto(analysisId);
  }
}
