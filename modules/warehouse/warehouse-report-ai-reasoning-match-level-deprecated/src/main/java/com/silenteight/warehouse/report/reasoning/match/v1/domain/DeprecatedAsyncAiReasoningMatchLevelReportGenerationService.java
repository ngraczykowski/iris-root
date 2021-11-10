package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.reasoning.match.v1.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.match.v1.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedAiReasoningMatchLevelReportGenerationService;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class DeprecatedAsyncAiReasoningMatchLevelReportGenerationService {

  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportRepository repository;
  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportGenerationService reportGenerationService;
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
    DeprecatedAiReasoningMatchLevelReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    DeprecatedAiReasoningMatchLevelReportDefinition reportType = report.getReportType();
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
    DeprecatedAiReasoningMatchLevelReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
