package com.silenteight.warehouse.report.billing.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;

@Slf4j
@RequiredArgsConstructor
class DownloadBillingReportUseCase {

  @NonNull
  private final ReportDataQuery reportDataQuery;
  @NonNull
  private final BillingReportService reportService;

  ReportDto activate(Long id) {
    log.debug("Getting Billing report, reportId={}", id);
    ReportDto reportDto = reportDataQuery.getReport(id);
    log.debug("Report downloaded, reportName={}", reportDto.getFilename());
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return reportDto;
  }
}
