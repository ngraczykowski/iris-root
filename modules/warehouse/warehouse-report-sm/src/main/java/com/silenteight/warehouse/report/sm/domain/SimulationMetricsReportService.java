package com.silenteight.warehouse.report.sm.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
public class SimulationMetricsReportService implements ReportsRemoval {

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

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    long numberOfRemovedReports = repository.removeAllByCreatedAtBefore(dayToRemoveReports);
    log.info(
        "Number of removed Simulation Metrics reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }
}
