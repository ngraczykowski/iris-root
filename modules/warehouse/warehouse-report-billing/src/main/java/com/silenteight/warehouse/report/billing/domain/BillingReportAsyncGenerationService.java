package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.billing.generation.BillingReportGenerationService;
import com.silenteight.warehouse.report.billing.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.BillingReportProperties;
import com.silenteight.warehouse.report.reporting.InMemoryReportDto;
import com.silenteight.warehouse.report.reporting.Report;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.List;
import javax.validation.Valid;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toInputStream;

@RequiredArgsConstructor
@Slf4j
class BillingReportAsyncGenerationService {

  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportGenerationService reportGenerationService;
  @Valid
  @NonNull
  private final BillingReportProperties properties;
  @NonNull
  private final ReportStorage reportStorage;

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
    log.debug("Generating Billing report with id={}", id);
    String fileStorageName = report.getFileStorageName();

    if (properties.isUseSqlReports()) {
      reportGenerationService.generateReport(range, fileStorageName);
    } else {
      generateElasticSearchReport(range, indexes, fileStorageName);
    }

    report.done();
    repository.save(report);
  }

  private void generateElasticSearchReport(
      ReportRange range, List<String> indexes, String fileStorageName) {

    CsvReportContentDto reportContentDto = reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes);

    InputStream reportContent = toInputStream(reportContentDto.getReport(), UTF_8);
    reportStorage.saveReport(toReport(fileStorageName, reportContent));
  }

  private static Report toReport(String name, InputStream inputStream) {
    return InMemoryReportDto.builder()
        .reportName(name)
        .inputStream(inputStream)
        .build();
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
