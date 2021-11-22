package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;
import com.silenteight.warehouse.report.rbs.download.dto.DownloadRbsReportDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

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

  public DownloadRbsReportDto activate(long id) {
    log.debug("Getting RB Scorer report, reportId={}", id);
    RbsReportDto rbsReportDto = reportDataQuery.getRbsReport(id);
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    return DownloadRbsReportDto.builder()
        .name(getFileName(rbsReportDto))
        .content(rbsReportDto.getContent())
        .build();
  }

  private String getFileName(RbsReportDto reportDto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(reportDto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(RbsReportDto reportDto) {
    ReportRange range = reportDto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(range.getFromAsLocalDate().toString())
        .to(range.getToAsLocalDate().toString())
        .timestamp(reportDto.getTimestamp())
        .build();
  }
}
