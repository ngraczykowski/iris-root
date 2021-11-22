package com.silenteight.warehouse.report.reasoning.match.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reasoning.match.domain.dto.AiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.reasoning.match.download.dto.DownloadAiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadSimulationAiReasoningMatchLevelReportUseCase {

  private static final String REPORT_TYPE = "AI_Reasoning_Match_Level";

  @NonNull
  private final AiReasoningMatchLevelReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;
  @NonNull
  private final ReportFileName reportFileName;

  public DownloadAiReasoningMatchLevelReportDto activate(long id, String analysisId) {
    log.debug("Getting simulation AI Reasoning Match Level report, reportId={}", id);
    AiReasoningMatchLevelReportDto dto = reportDataQuery.getAiReasoningReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting simulation AI Reasoning Match Level report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAiReasoningMatchLevelReportDto.builder()
        .name(getFileName(analysisId, dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(String analysisId, AiReasoningMatchLevelReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(analysisId, dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(
      String analysisId, AiReasoningMatchLevelReportDto dto) {

    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .analysisId(analysisId)
        .timestamp(dto.getTimestamp())
        .build();
  }
}
