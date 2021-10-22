package com.silenteight.warehouse.report.reasoning.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportGenerationService;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class AsyncAiReasoningReportGenerationService {

  @NonNull
  private final AiReasoningReportRepository repository;
  @NonNull
  private final AiReasoningReportGenerationService reportGenerationService;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningReportDefinitionProperties properties) {

    try {
      doGenerateReport(id, range, indexes, properties);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      long id,
      ReportRange range,
      List<String> indexes,
      AiReasoningReportDefinitionProperties properties) {

    AiReasoningReport report = repository.getById(id);
    report.generating();
    String fileStorageName = report.getFileStorageName();
    repository.save(report);
    log.debug("Generating report with id={}, fileStorageName={}", id, fileStorageName);

    reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes,
        properties,
        fileStorageName);

    report.done();
    repository.save(report);
    log.debug("Report generation done, id={}, fileStorageName={}", id, fileStorageName);
  }

  private void doFailReport(Long id) {
    AiReasoningReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
