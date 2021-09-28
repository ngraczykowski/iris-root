package com.silenteight.warehouse.report.accuracy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.accuracy.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportGenerationService;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class AsyncAccuracyReportGenerationService {

  @NonNull
  private final AccuracyReportRepository repository;
  @NonNull
  private final AccuracyReportGenerationService reportGenerationService;
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
    AccuracyReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    AccuracyReportDefinition reportType = report.getReportType();
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
    AccuracyReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
