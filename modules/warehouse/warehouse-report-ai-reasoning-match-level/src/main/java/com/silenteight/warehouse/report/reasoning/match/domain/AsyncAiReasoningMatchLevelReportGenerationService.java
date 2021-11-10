package com.silenteight.warehouse.report.reasoning.match.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportDefinitionProperties;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportGenerationService;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class AsyncAiReasoningMatchLevelReportGenerationService {

  @NonNull
  private final AiReasoningMatchLevelReportRepository repository;
  @NonNull
  private final AiReasoningMatchLevelReportGenerationService reportGenerationService;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningMatchLevelReportDefinitionProperties properties) {

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
      AiReasoningMatchLevelReportDefinitionProperties properties) {

    AiReasoningMatchLevelReport report = repository.getById(id);
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
    AiReasoningMatchLevelReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generation failed, id={}", id);
  }
}
