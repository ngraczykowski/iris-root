package com.silenteight.warehouse.report.reasoning.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.reasoning.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.v1.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reasoning.v1.generation.DeprecatedAiReasoningReportGenerationService;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class DeprecatedAsyncAiReasoningReportGenerationService {

  @NonNull
  private final DeprecatedAiReasoningReportRepository repository;
  @NonNull
  private final DeprecatedAiReasoningReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;

  @Async
  void generateReport(
      Long id, List<String> indexes, @Valid AiReasoningReportDefinitionProperties properties) {
    try {
      doGenerateReport(id, indexes, properties);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      Long id, List<String> indexes, AiReasoningReportDefinitionProperties properties) {
    DeprecatedAiReasoningReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    DeprecatedAiReasoningReportDefinition reportType = report.getReportType();
    String fileName = report.getFileStorageName();

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
    DeprecatedAiReasoningReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
