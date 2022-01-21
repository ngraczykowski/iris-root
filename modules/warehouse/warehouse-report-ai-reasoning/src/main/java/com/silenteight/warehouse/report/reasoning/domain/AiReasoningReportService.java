package com.silenteight.warehouse.report.reasoning.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReport.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AiReasoningReportService implements ReportsRemoval {

  @NonNull
  private final AiReasoningReportRepository repository;
  @NonNull
  private final AsyncAiReasoningReportGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningReportDefinitionProperties properties) {

    return createReportInstance(range, indexes, properties, null);
  }

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid AiReasoningReportDefinitionProperties properties,
      String analysisId) {

    AiReasoningReport report = of(range);
    AiReasoningReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(savedReport.getId(), range, indexes, properties,
        analysisId);
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

  private static List<String> getOutdatedReportsFileNames(List<AiReasoningReport> outdatedReports) {
    return outdatedReports.stream()
        .map(AiReasoningReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
