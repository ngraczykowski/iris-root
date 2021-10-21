package com.silenteight.warehouse.report.rbs.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbsReportDefinition;
import com.silenteight.warehouse.report.rbs.v1.generation.DeprecatedRbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.v1.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DeprecatedAsyncRbsReportGenerationService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final DeprecatedRbsReportRepository repository;
  @NonNull
  private final DeprecatedRbsReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final DeprecatedRbsReportDefinition productionReportProperties;
  @NonNull
  private final DeprecatedRbsReportDefinition simulationReportProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;

  @Async
  public void generateReport(long id) {
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    try {
      doGenerateReport(id, productionReportProperties, indexes);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(e);
    }
  }

  @Async
  public void generateReport(Long id, String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    try {
      doGenerateReport(id, simulationReportProperties, indexes);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(e);
    }
  }

  private void doGenerateReport(
      Long id, DeprecatedRbsReportDefinition properties, List<String> indexes) {
    DeprecatedRbsReport report = getReport(id);
    report.generating();
    repository.save(report);
    DeprecatedReportDefinition reportType = report.getReportType();
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        reportType.getFrom(timeSource.now()),
        reportType.getTo(timeSource.now()),
        indexes,
        properties);
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }

  private DeprecatedRbsReport getReport(long id) {
    try {
      return repository.getById(id);
    } catch (RuntimeException e) {
      log.error("Could not found report with id = {}.", id);
      throw e;
    }
  }

  private void doFailReport(Long id) {
    DeprecatedRbsReport rbsReport = repository.getById(id);
    rbsReport.failed();
    repository.save(rbsReport);
    log.warn("Rbs report generating failed, reportId={}", id);
  }
}
