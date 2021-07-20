package com.silenteight.warehouse.report.sm.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.sm.generation.SimulationMetricsReportGenerationService;
import com.silenteight.warehouse.report.sm.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class AsyncSimulationMetricsReportGenerationService {

  static final String SIMULATION_ANALYSIS_NAME = "simulation";
  @NonNull
  private final SimulationMetricsReportRepository repository;
  @NonNull
  private final SimulationMetricsReportGenerationService reportGenerationService;

  @Async
  public void generateReport(long id) {
    SimulationMetricsReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        report.getAnalysisId());
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }
}
