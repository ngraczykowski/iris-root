package com.silenteight.warehouse.report.accuracy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.accuracy.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportGenerationService;
import com.silenteight.warehouse.report.reporting.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportRange;

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

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AccuracyReportDefinitionProperties properties,
      String analysisId) {

    try {
      doGenerateReport(id, range, indexes, properties, analysisId);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      long id,
      ReportRange range,
      List<String> indexes,
      AccuracyReportDefinitionProperties properties,
      String analysisId) {

    AccuracyReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    String fileStorageName = report.getFileStorageName();

    reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes,
        properties,
        fileStorageName,
        analysisId);

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
