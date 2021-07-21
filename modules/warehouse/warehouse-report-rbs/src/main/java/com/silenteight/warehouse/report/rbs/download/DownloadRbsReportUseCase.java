package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

@Slf4j
@RequiredArgsConstructor
public class DownloadRbsReportUseCase {

  @NonNull
  private final RbsReportDataQuery reportDataQuery;
  @NonNull
  private final RbsReportService reportService;

  public ReportDto activate(long id) {
    log.debug("Getting RB Scorer report, reportId={}", id);
    ReportDto reportDto = reportDataQuery.getReport(id);
    log.debug("Report downloaded, reportName={}", reportDto.getFilename());
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return reportDto;
  }
}
