package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;
import com.silenteight.warehouse.report.rbs.download.dto.DownloadRbsReportDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadSimulationRbsReportUseCase {

  private static final String REPORT_TYPE = "RB_Scorer";

  @NonNull
  private final RbsReportDataQuery reportDataQuery;
  @NonNull
  private final RbsReportService reportService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final ReportStorage reportStorage;

  public DownloadRbsReportDto activate(long id, String analysisId) {
    log.debug("Getting RB Scorer report, reportId={}", id);
    RbsReportDto rbsReportDto = reportDataQuery.getRbsReport(id);
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);
    FileDto report = reportStorage.getReport(rbsReportDto.getFileName());
    return DownloadRbsReportDto.builder()
        .name(getFileName(rbsReportDto, analysisId))
        .content(report.getContent())
        .build();
  }

  private String getFileName(RbsReportDto rbsReportDto, String analysisId) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(rbsReportDto, analysisId);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(
      RbsReportDto rbsReportDto, String analysisId) {

    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .analysisId(analysisId)
        .timestamp(rbsReportDto.getTimestamp())
        .build();
  }
}
