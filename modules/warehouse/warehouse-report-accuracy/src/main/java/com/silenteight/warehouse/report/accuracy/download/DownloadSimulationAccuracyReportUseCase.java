package com.silenteight.warehouse.report.accuracy.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;
import com.silenteight.warehouse.report.accuracy.download.dto.DownloadAccuracyReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadSimulationAccuracyReportUseCase {

  private static final String REPORT_TYPE = "Accuracy";

  @NonNull
  private final AccuracyReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;
  @NonNull
  private final ReportFileName reportFileName;

  public DownloadAccuracyReportDto activate(long id, String analysisId) {
    log.debug("Getting simulation Accuracy report, reportId={}", id);
    AccuracyReportDto dto = reportDataQuery.getAccuracyReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting simulation Accuracy report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAccuracyReportDto.builder()
        .name(getFileName(analysisId, dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(String analysisId, AccuracyReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(analysisId, dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(String analysisId, AccuracyReportDto dto) {
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .analysisId(analysisId)
        .timestamp(dto.getTimestamp())
        .build();
  }
}
