package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.match.v1.generation.AiReasoningReportDefinitionProperties;
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
public class DeprecatedAiReasoningMatchLevelReportService implements ReportsRemoval {

  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportRepository repository;
  @NonNull
  private final DeprecatedAsyncAiReasoningMatchLevelReportGenerationService
      asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      DeprecatedAiReasoningMatchLevelReportDefinition definition,
      String analysisId,
      List<String> indexes,
      @Valid AiReasoningReportDefinitionProperties properties) {

    DeprecatedAiReasoningMatchLevelReport
        report = DeprecatedAiReasoningMatchLevelReport.of(definition, analysisId);
    DeprecatedAiReasoningMatchLevelReport savedReport = repository.save(report);
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
    log.info(
        "Number of removed AI Reasoning Match Level reports reportsCount={}",
        numberOfRemovedReports);

    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(
      List<DeprecatedAiReasoningMatchLevelReport> outdatedReports) {

    return outdatedReports.stream()
        .map(DeprecatedAiReasoningMatchLevelReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
