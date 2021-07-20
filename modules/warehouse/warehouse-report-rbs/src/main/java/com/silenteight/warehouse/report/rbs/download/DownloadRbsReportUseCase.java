package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

@RequiredArgsConstructor
public class DownloadRbsReportUseCase {

  @NonNull
  private final RbsReportDataQuery reportDataQuery;
  @NonNull
  private final RbsReportService reportService;

  public ReportDto activate(long id) {
    ReportDto reportDto = reportDataQuery.getReport(id);
    reportService.removeReport(id);
    return reportDto;
  }
}
