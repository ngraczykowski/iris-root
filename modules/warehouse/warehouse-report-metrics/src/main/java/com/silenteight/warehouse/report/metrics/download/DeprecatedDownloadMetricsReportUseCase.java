package com.silenteight.warehouse.report.metrics.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportService;
import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedDownloadMetricsReportUseCase {

  @NonNull
  private final DeprecatedMetricsReportDataQuery reportDataQuery;
  @NonNull
  private final DeprecatedMetricsReportService reportService;

  public ReportDto activate(long id) {
    log.debug("Getting report, reportId={}", id);
    ReportDto reportDto = reportDataQuery.getReport(id);
    log.debug("Report downloaded, reportName={}", reportDto.getFilename());
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return reportDto;
  }
}
