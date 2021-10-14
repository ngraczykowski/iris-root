package com.silenteight.warehouse.report.simulation.v1;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;

public interface DeprecatedSimulationReportsProvider {

  List<ReportDefinitionDto> getReportDefinitions(String analysisId);
}
