package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

import static java.time.OffsetDateTime.ofInstant;

@RequiredArgsConstructor
@Slf4j
public class AsyncRbsReportGenerationService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final RbsReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;

  @Async
  public void generateReport(long id) {
    RbsReport report = getReport(id);
    report.generating();
    repository.save(report);
    ReportDefinition reportType = report.getReportType();
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        ofInstant(reportType.getFrom(timeSource.now()), timeSource.timeZone().toZoneId()),
        ofInstant(reportType.getTo(timeSource.now()), timeSource.timeZone().toZoneId()),
        PRODUCTION_ANALYSIS_NAME);
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }

  private RbsReport getReport(long id) {
    try {
      return repository.getById(id);
    } catch (RuntimeException e) {
      log.error("Could not found report with id = {}.", id);
      throw e;
    }
  }
}
