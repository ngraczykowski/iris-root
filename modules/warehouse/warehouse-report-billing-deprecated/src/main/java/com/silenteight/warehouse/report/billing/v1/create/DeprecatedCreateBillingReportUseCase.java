package com.silenteight.warehouse.report.billing.v1.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.v1.domain.DeprecatedBillingReportService;
import com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class DeprecatedCreateBillingReportUseCase {

  @NonNull
  private final DeprecatedBillingReportService reportService;

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    return reportService.createReportInstance(DeprecatedReportDefinition.getReportType(reportId));
  }
}
