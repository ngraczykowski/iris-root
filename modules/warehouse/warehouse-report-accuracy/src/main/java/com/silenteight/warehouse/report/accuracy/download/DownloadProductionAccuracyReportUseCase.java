package com.silenteight.warehouse.report.accuracy.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;
import com.silenteight.warehouse.report.accuracy.download.dto.DownloadAccuracyReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionAccuracyReportUseCase {

  private static final String REPORT_TYPE = "Accuracy";

  @NonNull
  private final AccuracyReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final DateFormatter dateFormatter;

  public DownloadAccuracyReportDto activate(long id) {
    log.debug("Getting production Accuracy report, reportId={}", id);
    AccuracyReportDto dto = reportDataQuery.getAccuracyReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting production Accuracy report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAccuracyReportDto.builder()
        .name(getFileName(dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(AccuracyReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private ReportFileNameDto toReportFileNameDto(AccuracyReportDto dto) {
    ReportRange range = dto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(dateFormatter.format(range.getFrom()))
        .to(dateFormatter.format(range.getTo()))
        .timestamp(dto.getTimestamp())
        .build();
  }
}
