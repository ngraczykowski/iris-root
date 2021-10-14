package com.silenteight.warehouse.report.simulation.v1;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class DeprecatedSimulationReportsDefinitionsUseCase {

  @NonNull
  private final List<DeprecatedSimulationReportsProvider> deprecatedSimulationReportsProviders;

  List<ReportDefinitionDto> activate(String analysisId) {
    return deprecatedSimulationReportsProviders
        .stream()
        .map(reportDefinitions -> reportDefinitions.getReportDefinitions(analysisId))
        .flatMap(Collection::stream)
        .collect(toList());
  }
}
