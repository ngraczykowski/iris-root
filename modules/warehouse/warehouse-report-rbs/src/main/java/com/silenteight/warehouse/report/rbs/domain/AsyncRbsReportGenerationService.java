package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.report.rbs.domain.exception.ReportGenerationException;
import com.silenteight.warehouse.report.reporting.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportGenerationService;
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
  private final ReportGenerationService reportGenerationService;

  @Async
  public void generateReport(
      long id,
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid RbsReportDefinition properties,
      String fileName,
      String analysisId) {

    try {
      doGenerateReport(id, range, indexes, properties, fileName, analysisId);
    } catch (RuntimeException e) {
      doFailReport(id);
      throw new ReportGenerationException(id, e);
    }
  }

  private void doGenerateReport(
      long id,
      ReportRange range,
      List<String> indexes,
      @Valid RbsReportDefinition properties, String fileName, String analysisId) {

    RbsReport report = repository.getById(id);
    report.generating();
    repository.save(report);
    FetchDataRequest dataRequest = FetchDataRequest.builder()
        .indexes(indexes)
        .useSqlReports(properties.isUseSqlReports())
        .sqlTemplates(properties.getSqlTemplates())
        .selectSqlQuery(properties.getSelectSqlQuery())
        .from(range.getFrom())
        .name(fileName)
        .to(range.getTo())
        .analysisId(analysisId).build();
    reportGenerationService.generate(dataRequest);

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
