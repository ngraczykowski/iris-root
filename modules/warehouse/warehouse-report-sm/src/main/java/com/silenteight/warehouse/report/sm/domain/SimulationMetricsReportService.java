package com.silenteight.warehouse.report.sm.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class SimulationMetricsReportService {

  @NonNull
  SimulationMetricsReportRepository repository;

  @NonNull
  AsyncSimulationMetricsReportGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(
      ReportDefinition definition, String analysisId) {
    SimulationMetricsReport report = SimulationMetricsReport.of(definition, analysisId);
    SimulationMetricsReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(savedReport.getId());
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }
}
