package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.rbs.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.rbs.generation.RbsReportGenerationService;
import com.silenteight.warehouse.report.rbs.generation.dto.CsvReportContentDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
class AsyncRbsReportGenerationService {

  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final RbsReportGenerationService reportGenerationService;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid RbsReportDefinition properties) {

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
      @Valid RbsReportDefinition properties) {

    RbsReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    CsvReportContentDto reportContent = reportGenerationService.generateReport(
        range.getFrom(),
        range.getTo(),
        indexes,
        properties);

    report.storeReport(reportContent.getReport());
    report.done();
    repository.save(report);
  }

  private void doFailReport(Long id) {
    RbsReport rbsReport = repository.getById(id);
    rbsReport.failed();
    repository.save(rbsReport);
    log.warn("Rbs report generating failed, reportId={}", id);
  }
}
