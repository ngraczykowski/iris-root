package com.silenteight.warehouse.report.metrics.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.domain.dto.MetricsReportDto;
import com.silenteight.warehouse.report.metrics.download.dto.DownloadMetricsReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;

@Slf4j
@RequiredArgsConstructor
class DownloadSimulationMetricsReportUseCase {

  private static final String REPORT_TYPE = "Metrics";

  @NonNull
  private final MetricsReportDataQuery reportDataQuery;
  @NonNull
  private final MetricsReportService reportService;
  @NonNull
  private final ReportFileName reportFileName;

  DownloadMetricsReportDto activate(long id, String analysisId) {
    log.debug("Getting simulation Metrics report, reportId={}", id);
    MetricsReportDto dto = reportDataQuery.getReport(id);
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return DownloadMetricsReportDto.builder()
        .name(getFileName(analysisId, dto))
        .content(dto.getContent())
        .build();
  }

  private String getFileName(String analysisId, MetricsReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(analysisId, dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(String analysisId, MetricsReportDto dto) {
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .analysisId(analysisId)
        .timestamp(dto.getTimestamp())
        .build();
  }
}
