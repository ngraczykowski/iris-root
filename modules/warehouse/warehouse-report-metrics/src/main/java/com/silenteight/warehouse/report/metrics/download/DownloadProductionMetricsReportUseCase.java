package com.silenteight.warehouse.report.metrics.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.domain.dto.MetricsReportDto;
import com.silenteight.warehouse.report.metrics.download.dto.DownloadMetricsReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionMetricsReportUseCase {

  private static final String REPORT_TYPE = "Metrics";

  @NonNull
  private final MetricsReportDataQuery reportDataQuery;
  @NonNull
  private final MetricsReportService reportService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final DateFormatter dateFormatter;
  @NonNull
  private final ReportStorage reportStorageService;

  DownloadMetricsReportDto activate(long id) {
    log.debug("Getting production Metrics report, reportId={}", id);
    MetricsReportDto dto = reportDataQuery.getReport(id);
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);

    FileDto report = reportStorageService.getReport(dto.getFileStorageName());

    return DownloadMetricsReportDto.builder()
        .name(getFileName(dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(MetricsReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private ReportFileNameDto toReportFileNameDto(MetricsReportDto dto) {
    ReportRange range = dto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(dateFormatter.format(range.getFrom()))
        .to(dateFormatter.format(range.getTo()))
        .timestamp(dto.getTimestamp())
        .build();
  }
}
