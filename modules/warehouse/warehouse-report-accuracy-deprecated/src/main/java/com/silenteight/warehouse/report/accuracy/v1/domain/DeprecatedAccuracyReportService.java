package com.silenteight.warehouse.report.accuracy.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.accuracy.v1.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedAccuracyReportService implements ReportsRemoval {

  @NonNull
  private final DeprecatedAccuracyReportRepository repository;
  @NonNull
  private final DeprecatedAsyncAccuracyReportGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      DeprecatedAccuracyReportDefinition definition,
      String analysisId,
      List<String> indexes,
      AccuracyReportDefinitionProperties properties) {

    DeprecatedAccuracyReport report = DeprecatedAccuracyReport.of(definition, analysisId);
    DeprecatedAccuracyReport savedReport = repository.save(report);
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
    log.info("Number of removed Accuracy reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(
      List<DeprecatedAccuracyReport> outdatedReports) {
    return outdatedReports.stream()
        .map(DeprecatedAccuracyReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
