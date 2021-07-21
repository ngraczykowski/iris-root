package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AsyncRbsReportGenerationService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final RbsReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final RbsReportDefinition productionReportProperties;
  @NonNull
  private final RbsReportDefinition simulationReportProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;

  @Async
  public void generateReport(long id) {
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    doGenerateReport(id, productionReportProperties, indexes);
  }

  @Async
  public void generateReport(Long id, String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    doGenerateReport(id, simulationReportProperties, indexes);
  }

  private void doGenerateReport(
      Long id, RbsReportDefinition properties, List<String> indexes) {
    RbsReport report = getReport(id);
    report.generating();
    repository.save(report);
    ReportDefinition reportType = report.getReportType();
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        reportType.getFrom(timeSource.now()),
        reportType.getTo(timeSource.now()),
        indexes,
        properties);
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }

  private RbsReport getReport(long id) {
    try {
      return repository.getById(id);
    } catch (RuntimeException e) {
      log.error("Could not found report with id = {}.", id);
      throw e;
    }
  }
}
