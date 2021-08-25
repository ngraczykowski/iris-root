package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public class BillingReportAsyncGenerationService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportGenerationService reportGenerationService;

  @Async
  public void generateReport(long id) {
    try {
      doGenerateReport(id);
    } catch (RuntimeException e) {
      doFailReport(id);
      log.error("Error occurred during the report generating process", e);
      throw new ReportGenerationException(e);
    }
  }

  private void doGenerateReport(long id) {
    BillingReport report = getReport(id);
    report.generating();
    repository.save(report);
    ReportDefinition reportType = report.getReportType();
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        reportType.getFrom(), reportType.getTo(), PRODUCTION_ANALYSIS_NAME);
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }

  private BillingReport getReport(long id) {
    try {
      return repository.getById(id);
    } catch (RuntimeException e) {
      log.error("Could not found report with id = {}.", id);
      throw e;
    }
  }

  private void doFailReport(long id) {
    BillingReport report = getReport(id);
    report.failed();
    repository.save(report);
    log.warn("Billing report generating failed, reportId={}", id);
  }
}
