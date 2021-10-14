package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.metrics.domain.MetricsReport.of;

@Slf4j
@RequiredArgsConstructor
public class MetricsReportService implements ReportsRemoval {

  @NonNull
  private final MetricsReportRepository repository;
  @NonNull
  private final AsyncMetricsReportGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull String fileName,
      @NonNull List<String> indexesForAnalysis,
      @NonNull @Valid PropertiesDefinition properties) {

    MetricsReport report = of(fileName);
    MetricsReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(
        savedReport.getId(), range, indexesForAnalysis, properties);

    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    long numberOfRemovedReports = repository.removeAllByCreatedAtBefore(dayToRemoveReports);
    log.info("Number of removed Metrics reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }
}
