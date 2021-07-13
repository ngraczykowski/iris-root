package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class RbsReportService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final AsyncRbsReportGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(ReportDefinition reportType) {
    Report report = Report.of(reportType);
    Report savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId());
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(Long id) {
    repository.deleteById(id);
  }
}
