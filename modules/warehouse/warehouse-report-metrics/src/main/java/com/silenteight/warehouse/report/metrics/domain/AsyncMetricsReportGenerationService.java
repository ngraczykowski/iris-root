package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class AsyncMetricsReportGenerationService {

  @NonNull
  private final MetricsReportRepository repository;
  @NonNull
  private final MetricsReportGenerationService reportGenerationService;

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
    repository.save(report);

    log.debug("Generating report with id={}", id);
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes,
        properties);

    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
    log.debug("Report generating done, id={}", id);
  }

  private void doFailReport(Long id) {
    MetricsReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generating failed, id={}", id);
  }
}
