package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;
import com.silenteight.warehouse.report.rbs.download.dto.DownloadRbsReportDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionRbsReportUseCase {

  private static final String REPORT_TYPE = "RB_Scorer";

  @NonNull
  private final RbsReportDataQuery reportDataQuery;
  @NonNull
  private final RbsReportService reportService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final DateFormatter dateFormatter;
  @NonNull
  private final ReportStorage reportStorage;

  public DownloadRbsReportDto activate(long id) {
    log.debug("Getting RB Scorer report, reportId={}", id);
    RbsReportDto rbsReportDto = reportDataQuery.getRbsReport(id);
    reportService.removeReport(id);
    FileDto report = reportStorage.getReport(rbsReportDto.getFileName());
    log.debug("Report removed, reportId={}", id);
    return DownloadRbsReportDto.builder()
        .name(getFileName(rbsReportDto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(RbsReportDto reportDto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(reportDto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private ReportFileNameDto toReportFileNameDto(RbsReportDto reportDto) {
    ReportRange range = reportDto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(dateFormatter.format(range.getFrom()))
        .to(dateFormatter.format(range.getTo()))
        .timestamp(reportDto.getTimestamp())
        .build();
  }
}
