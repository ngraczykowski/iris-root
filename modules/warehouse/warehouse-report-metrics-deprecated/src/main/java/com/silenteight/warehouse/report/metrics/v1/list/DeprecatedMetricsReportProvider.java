package com.silenteight.warehouse.report.metrics.v1.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.simulation.v1.DeprecatedSimulationReportsProvider;

import java.util.List;

import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.toReportsDefinitionDto;

class DeprecatedMetricsReportProvider implements DeprecatedSimulationReportsProvider {

  @Override
  public List<ReportDefinitionDto> getReportDefinitions(String analysisId) {
    return toReportsDefinitionDto(analysisId);
  }
}
