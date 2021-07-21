package com.silenteight.warehouse.report.sm.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;
import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

@Slf4j
@RequiredArgsConstructor
public class DownloadSimulationMetricsReportUseCase {

  @NonNull
  private final SimulationMetricsReportDataQuery reportDataQuery;
  @NonNull
  private final SimulationMetricsReportService reportService;

  public ReportDto activate(long id) {
    log.debug("Getting report, reportId={}", id);
    ReportDto reportDto = reportDataQuery.getReport(id);
    log.debug("Report downloaded, reportName={}", reportDto.getFilename());
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return reportDto;
  }
}
