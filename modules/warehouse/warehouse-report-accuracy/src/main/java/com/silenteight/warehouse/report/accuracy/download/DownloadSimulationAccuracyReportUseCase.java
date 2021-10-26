package com.silenteight.warehouse.report.accuracy.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;
import com.silenteight.warehouse.report.accuracy.download.dto.DownloadAccuracyReportDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class DownloadSimulationAccuracyReportUseCase {

  private static final String FILE_NAME = "simulation_%s_Accuracy.csv";

  @NonNull
  private final AccuracyReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public DownloadAccuracyReportDto activate(long id, String analysisId) {
    log.debug("Getting Accuracy report, reportId={}", id);
    AccuracyReportDto dto = reportDataQuery.getAccuracyReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting Accuracy report from storage, fileStorageName={}", fileStorageName);
    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAccuracyReportDto.builder()
        .name(getFileName(analysisId))
        .content(report.getContent())
        .build();
  }

  private static String getFileName(String analysisId) {
    return format(FILE_NAME, analysisId);
  }
}
