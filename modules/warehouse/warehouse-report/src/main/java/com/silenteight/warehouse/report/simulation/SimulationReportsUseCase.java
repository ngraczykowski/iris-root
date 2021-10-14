package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SimulationReportsUseCase {

  @NonNull
  private final List<SimulationReportProvider> simulationReportProviders;

  List<ReportTypeDto> activate(String analysisId) {
    return simulationReportProviders
        .stream()
        .map(reportDefinitions -> reportDefinitions.getReport(analysisId))
        .collect(toList());
  }
}
