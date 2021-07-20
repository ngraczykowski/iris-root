package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
class SimulationReportsDefinitionsUseCase {

  @NonNull
  private final SimulationReportingQuery simulationReportingQuery;
  @NonNull
  private final List<SimulationReportsProvider> simulationReportsProviders;

  List<ReportDefinitionDto> activate(String analysisId) {
    List<ReportDefinitionDto> kibanaReports = simulationReportingQuery
        .getReportsDefinitions(analysisId);

    List<ReportDefinitionDto> predefinedReports = simulationReportsProviders
        .stream()
        .map(reportDefinitions -> reportDefinitions.getReportDefinitions(analysisId))
        .flatMap(Collection::stream)
        .collect(toList());

    return concat(predefinedReports.stream(), kibanaReports.stream()).collect(toList());
  }

}
