package com.silenteight.warehouse.report.billing.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class CreateBillingReportUseCase {

  @NonNull
  private final BillingReportService reportService;

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    return reportService.createReportInstance(ReportDefinition.getReportType(reportId));
  }
}
