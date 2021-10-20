package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.util.List;

@RequiredArgsConstructor
public class BillingReportService {

  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportAsyncGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull String fileName,
      @NonNull List<String> indexes) {

    BillingReport report = BillingReport.of(fileName);
    BillingReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId(), range, indexes);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(Long id) {
    repository.deleteById(id);
  }
}
