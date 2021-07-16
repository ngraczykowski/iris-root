package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class BillingReportService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportAsyncGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(ReportDefinition reportType) {
    BillingReport report = BillingReport.of(reportType);
    BillingReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId());
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(Long id) {
    repository.deleteById(id);
  }
}
