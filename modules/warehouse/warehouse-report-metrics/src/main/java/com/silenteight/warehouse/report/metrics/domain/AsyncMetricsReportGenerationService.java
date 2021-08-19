package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.metrics.generation.MetricsReportGenerationService;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.metrics.generation.dto.CsvReportContentDto;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
public class AsyncMetricsReportGenerationService {

  @NonNull
  private final MetricsReportRepository repository;
  @NonNull
  private final MetricsReportGenerationService reportGenerationService;
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
    MetricsReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    log.debug("Generating report with id={}", id);
    MetricsReportDefinition reportType = report.getReportType();
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
    MetricsReport report = repository.getById(id);
    report.failed();
    repository.save(report);
    log.debug("Report generating failed, id={}", id);
  }
}
