package com.silenteight.warehouse.report.billing.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedBillingReport.of;

@RequiredArgsConstructor
public class DeprecatedBillingReportService {

  static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final DeprecatedBillingReportRepository repository;
  @NonNull
  private final DeprecatedBillingReportAsyncGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(DeprecatedReportDefinition reportType) {
    DeprecatedBillingReport report = of(reportType);
    DeprecatedBillingReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId());
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(Long id) {
    repository.deleteById(id);
  }
}
