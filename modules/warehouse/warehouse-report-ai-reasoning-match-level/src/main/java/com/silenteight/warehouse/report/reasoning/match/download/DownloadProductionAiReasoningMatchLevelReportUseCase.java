package com.silenteight.warehouse.report.reasoning.match.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.reasoning.match.domain.dto.AiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.reasoning.match.download.dto.DownloadAiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionAiReasoningMatchLevelReportUseCase {

  private static final String FILE_NAME = "AI_Reasoning_Match_Level_%s_To_%s.csv";

  @NonNull
  private final AiReasoningMatchLevelReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public DownloadAiReasoningMatchLevelReportDto activate(long id) {
    log.debug("Getting AI Reasoning Match Level report, reportId={}", id);
    AiReasoningMatchLevelReportDto dto = reportDataQuery.getAiReasoningReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting AI Reasoning Match Level report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAiReasoningMatchLevelReportDto.builder()
        .name(getFileName(dto.getRange()))
        .content(report.getContent())
        .build();
  }

  private static String getFileName(ReportRange range) {
    return format(FILE_NAME, range.getFromAsLocalDate(), range.getToAsLocalDate());
  }
}
