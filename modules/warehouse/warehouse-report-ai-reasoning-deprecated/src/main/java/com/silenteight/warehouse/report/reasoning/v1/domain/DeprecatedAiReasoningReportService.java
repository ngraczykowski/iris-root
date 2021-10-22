package com.silenteight.warehouse.report.reasoning.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.v1.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedAiReasoningReportService implements ReportsRemoval {

  @NonNull
  private final DeprecatedAiReasoningReportRepository repository;
  @NonNull
  private final DeprecatedAsyncAiReasoningReportGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      DeprecatedAiReasoningReportDefinition definition,
      String analysisId,
      List<String> indexes,
      @Valid AiReasoningReportDefinitionProperties properties) {

    DeprecatedAiReasoningReport report = DeprecatedAiReasoningReport.of(definition, analysisId);
    DeprecatedAiReasoningReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(savedReport.getId(), indexes, properties);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    var outdatedReports = repository.getAllByCreatedAtBefore(dayToRemoveReports);
    List<String> outdatedReportsFileNames = getOutdatedReportsFileNames(outdatedReports);
    reportStorage.removeReports(outdatedReportsFileNames);
    repository.deleteAll(outdatedReports);
    int numberOfRemovedReports = outdatedReports.size();
    log.info("Number of removed AI Reasoning reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(
      List<DeprecatedAiReasoningReport> outdatedReports) {

    return outdatedReports.stream()
        .map(DeprecatedAiReasoningReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
