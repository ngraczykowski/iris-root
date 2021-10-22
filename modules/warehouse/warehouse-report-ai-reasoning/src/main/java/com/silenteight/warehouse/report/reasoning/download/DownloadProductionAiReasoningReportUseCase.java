package com.silenteight.warehouse.report.reasoning.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.reasoning.domain.dto.AiReasoningReportDto;
import com.silenteight.warehouse.report.reasoning.download.dto.DownloadAiReasoningReportDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionAiReasoningReportUseCase {

  private static final String FILE_NAME = "AI_Reasoning_%s_To_%s.csv";

  @NonNull
  private final AiReasoningReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public DownloadAiReasoningReportDto activate(long id) {
    log.debug("Getting AI Reasoning report, reportId={}", id);
    AiReasoningReportDto dto = reportDataQuery.getAiReasoningReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting AI Reasoning report from storage, fileStorageName={}", fileStorageName);
    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAiReasoningReportDto.builder()
        .name(getFileName(dto.getRange()))
        .content(report.getContent())
        .build();
  }

  private static String getFileName(ReportRange range) {
    return format(FILE_NAME, range.getFromAsLocalDate(), range.getToAsLocalDate());
  }
}
