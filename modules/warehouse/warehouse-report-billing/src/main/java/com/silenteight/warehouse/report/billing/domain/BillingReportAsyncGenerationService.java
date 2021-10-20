package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class BillingReportAsyncGenerationService {

  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportGenerationService reportGenerationService;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes) {

    try {
      doGenerateReport(id, range, indexes);
    } catch (RuntimeException e) {
      doFailReport(id);
      log.error("Error occurred during the report generating process", e);
      throw new ReportGenerationException(e);
    }
  }

  private void doGenerateReport(long id, ReportRange range, List<String> indexes) {
    BillingReport report = getReport(id);
    report.generating();
    repository.save(report);
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes);

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
