package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.Report;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.List;
import javax.validation.Valid;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.toInputStream;

@Slf4j
@RequiredArgsConstructor
class AsyncMetricsReportGenerationService {

  @NonNull
  private final MetricsReportRepository repository;
  @NonNull
  private final MetricsReportGenerationService reportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @Valid PropertiesDefinition properties) {

    try {
      doGenerateReport(id, range, indexes, properties);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      long id,
      ReportRange range,
      List<String> indexes,
      PropertiesDefinition properties) {

    MetricsReport report = repository.getById(id);
    report.generating();
    String fileStorageName = report.getFileStorageName();
    repository.save(report);

    log.debug("Generating report with id={}", id);

    if (properties.isUseSqlReports()) {
      generatePsql(range, properties, fileStorageName);
    } else {
      generateEs(range, properties, indexes, fileStorageName);
    }

    report.done();
    repository.save(report);
    log.debug("Report generating done, id={}", id);
  }

  private void generatePsql(
      ReportRange range,
      PropertiesDefinition properties,
      String fileStorageName) {
    reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        properties,
        fileStorageName);
  }

  private void generateEs(
      ReportRange range,
      PropertiesDefinition properties,
      List<String> indexes,
      String fileStorageName) {

    CsvReportContentDto report = reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes,
        properties);

    InputStream reportStream = toInputStream(report.getReport(), UTF_8);
    reportStorage.saveReport(new MetricsReportStorageDto(reportStream, fileStorageName));
  }

  @Value
  private static class MetricsReportStorageDto implements Report {

    InputStream inputStream;
    String reportName;
  }

  private void doFailReport(Long id) {
    MetricsReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generating failed, id={}", id);
  }
}
