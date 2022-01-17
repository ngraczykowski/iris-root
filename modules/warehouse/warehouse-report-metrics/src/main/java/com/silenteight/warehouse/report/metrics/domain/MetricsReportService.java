package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.metrics.domain.MetricsReport.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class MetricsReportService implements ReportsRemoval {

  @NonNull
  private final MetricsReportRepository repository;
  @NonNull
  private final AsyncMetricsReportGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexesForAnalysis,
      @NonNull @Valid PropertiesDefinition properties,
      String analysisId) {

    MetricsReport report = of(range);
    MetricsReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(
        savedReport.getId(), range, indexesForAnalysis, properties, analysisId);

    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    var outdatedReports = repository.getAllByCreatedAtBefore(dayToRemoveReports);
    List<String> outdatedReportsFileNames = getOutdatedReportsFileNames(outdatedReports);
    reportStorage.removeReports(outdatedReportsFileNames);
    repository.deleteAll(outdatedReports);
    int numberOfRemovedReports = outdatedReports.size();
    log.info("Number of removed Metrics reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(List<MetricsReport> outdatedReports) {
    return outdatedReports.stream()
        .map(MetricsReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }

}
