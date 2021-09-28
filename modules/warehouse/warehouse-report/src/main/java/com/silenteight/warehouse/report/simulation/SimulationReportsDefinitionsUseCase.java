package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SimulationReportsDefinitionsUseCase {

  @NonNull
  private final List<SimulationReportsProvider> simulationReportsProviders;

  List<ReportDefinitionDto> activate(String analysisId) {
    return simulationReportsProviders
        .stream()
        .map(reportDefinitions -> reportDefinitions.getReportDefinitions(analysisId))
        .flatMap(Collection::stream)
        .collect(toList());
  }
}
