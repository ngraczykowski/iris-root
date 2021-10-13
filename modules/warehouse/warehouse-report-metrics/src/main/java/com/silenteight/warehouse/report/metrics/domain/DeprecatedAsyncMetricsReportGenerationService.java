package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.DeprecatedMetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedAsyncMetricsReportGenerationService {

  @NonNull
  private final DeprecatedMetricsReportRepository repository;
  @NonNull
  private final DeprecatedMetricsReportGenerationService reportGenerationService;
  @NonNull
  private final TimeSource timeSource;

  @Async
  public void generateReport(
      Long id, List<String> indexes, @Valid PropertiesDefinition properties) {
    try {
      doGenerateReport(id, indexes, properties);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(Long id, List<String> indexes, PropertiesDefinition properties) {
    DeprecatedMetricsReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    DeprecatedMetricsReportDefinition reportType = report.getReportType();
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        reportType.getFrom(timeSource.now()),
        reportType.getTo(timeSource.now()),
        indexes,
        properties);
    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
    log.debug("Report generating done, id={}", id);
  }

  private void doFailReport(Long id) {
    DeprecatedMetricsReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generating failed, id={}", id);
  }
}
