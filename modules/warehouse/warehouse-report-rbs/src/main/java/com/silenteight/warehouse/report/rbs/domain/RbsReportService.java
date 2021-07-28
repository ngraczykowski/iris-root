package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Slf4j
public class RbsReportService implements ReportsRemoval {

  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final AsyncRbsReportGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createProductionReportInstance(ReportDefinition reportType) {
    RbsReport report = RbsReport.of(reportType);
    RbsReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId());
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }

  public ReportInstanceReferenceDto createSimulationReportInstance(
      String analysisId, ReportDefinition reportType) {

    RbsReport report = RbsReport.of(reportType);
    RbsReport savedReport = repository.save(report);

    asyncReportGenerationService.generateReport(savedReport.getId(), analysisId);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    long numberOfRemovedReports = repository.deleteByCreatedAtBefore(dayToRemoveReports);
    log.info("Number of removed RB Scorer reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }

}
