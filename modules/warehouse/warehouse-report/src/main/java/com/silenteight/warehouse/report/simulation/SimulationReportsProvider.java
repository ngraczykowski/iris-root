package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;

public interface SimulationReportsProvider {

  List<ReportDefinitionDto> getReportDefinitions(String analysisId);
}
