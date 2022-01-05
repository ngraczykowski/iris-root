package com.silenteight.warehouse.report.reasoning.match.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.AiReasoningMatchLevelReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReport.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AiReasoningMatchLevelReportService implements ReportsRemoval {

  @NonNull
  private final AiReasoningMatchLevelReportRepository repository;
  @NonNull
  private final AsyncAiReasoningMatchLevelReportGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningMatchLevelReportDefinitionProperties properties) {

    AiReasoningMatchLevelReport report = of(range);
    AiReasoningMatchLevelReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(savedReport.getId(), range, indexes, properties);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    var outdatedReports = repository.getAllByCreatedAtBefore(dayToRemoveReports);
    List<String> outdatedReportsFileNames = getOutdatedReportsFileNames(outdatedReports);
    reportStorage.removeReports(outdatedReportsFileNames);
    repository.deleteAll(outdatedReports);
    int numberOfRemovedReports = outdatedReports.size();
    log.info("Number of removed AI Reasoning Match Level reports reportsCount={}",
        numberOfRemovedReports);

    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(
      List<AiReasoningMatchLevelReport> outdatedReports) {
    return outdatedReports.stream()
        .map(AiReasoningMatchLevelReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
