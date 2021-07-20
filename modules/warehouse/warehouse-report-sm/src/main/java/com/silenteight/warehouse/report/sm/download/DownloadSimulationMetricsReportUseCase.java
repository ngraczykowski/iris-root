package com.silenteight.warehouse.report.sm.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;
import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

@RequiredArgsConstructor
public class DownloadSimulationMetricsReportUseCase {

  @NonNull
  private final SimulationMetricsReportDataQuery reportDataQuery;
  @NonNull
  private final SimulationMetricsReportService reportService;

  public ReportDto activate(long id) {
    ReportDto reportDto = reportDataQuery.getReport(id);
    reportService.removeReport(id);
    return reportDto;
  }
}
