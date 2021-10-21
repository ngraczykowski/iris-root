package com.silenteight.warehouse.report.rbs.v1.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedRbsReportService;
import com.silenteight.warehouse.report.rbs.v1.domain.dto.ReportDto;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedDownloadRbsReportUseCase {

  @NonNull
  private final DeprecatedRbsReportDataQuery reportDataQuery;
  @NonNull
  private final DeprecatedRbsReportService reportService;

  public ReportDto activate(long id) {
    log.debug("Getting RB Scorer report, reportId={}", id);
    ReportDto reportDto = reportDataQuery.getReport(id);
    log.debug("Report downloaded, reportName={}", reportDto.getFilename());
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return reportDto;
  }
}
