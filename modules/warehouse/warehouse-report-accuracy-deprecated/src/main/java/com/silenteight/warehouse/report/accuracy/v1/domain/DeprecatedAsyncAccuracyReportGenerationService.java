package com.silenteight.warehouse.report.accuracy.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.accuracy.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.v1.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedAccuracyReportGenerationService;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class DeprecatedAsyncAccuracyReportGenerationService {

  @NonNull
  private final DeprecatedAccuracyReportRepository repository;
  @NonNull
  private final DeprecatedAccuracyReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;

  @Async
  void generateReport(
      Long id, List<String> indexes, @Valid AccuracyReportDefinitionProperties properties) {
    try {
      doGenerateReport(id, indexes, properties);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      Long id, List<String> indexes, AccuracyReportDefinitionProperties properties) {
    DeprecatedAccuracyReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    DeprecatedAccuracyReportDefinition reportType = report.getReportType();
    String fileName = report.getFileName();

    reportGenerationService.generateReport(
        reportType.getFrom(timeSource.now()),
        reportType.getTo(timeSource.now()),
        indexes,
        properties,
        fileName);

    report.storeReport(fileName);
    report.done();
    repository.save(report);
    log.debug("Report generation done, id={}", id);
  }

  private void doFailReport(Long id) {
    DeprecatedAccuracyReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
