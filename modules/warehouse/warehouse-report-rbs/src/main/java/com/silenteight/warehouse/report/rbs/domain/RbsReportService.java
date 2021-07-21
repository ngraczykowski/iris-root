package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class RbsReportService {

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
}
