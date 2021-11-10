package com.silenteight.warehouse.report.reasoning.match.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.reasoning.match.domain.dto.AiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.reasoning.match.download.dto.DownloadAiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class DownloadSimulationAiReasoningMatchLevelReportUseCase {

  private static final String FILE_NAME = "simulation_%s_AI_Reasoning_Match_Level.csv";

  @NonNull
  private final AiReasoningMatchLevelReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public DownloadAiReasoningMatchLevelReportDto activate(long id, String analysisId) {
    log.debug("Getting AI Reasoning Match Level report, reportId={}", id);
    AiReasoningMatchLevelReportDto dto = reportDataQuery.getAiReasoningReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting AI Reasoning Match Level report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAiReasoningMatchLevelReportDto.builder()
        .name(getFileName(analysisId))
        .content(report.getContent())
        .build();
  }

  private static String getFileName(String analysisId) {
    return format(FILE_NAME, analysisId);
  }
}
