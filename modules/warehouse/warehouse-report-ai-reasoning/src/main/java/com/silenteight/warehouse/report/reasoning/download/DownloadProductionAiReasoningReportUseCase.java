package com.silenteight.warehouse.report.reasoning.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reasoning.domain.dto.AiReasoningReportDto;
import com.silenteight.warehouse.report.reasoning.download.dto.DownloadAiReasoningReportDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadProductionAiReasoningReportUseCase {

  private static final String REPORT_TYPE = "AI_Reasoning";

  @NonNull
  private final AiReasoningReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final DateFormatter dateFormatter;

  public DownloadAiReasoningReportDto activate(long id) {
    log.debug("Getting production AI Reasoning report, reportId={}", id);
    AiReasoningReportDto dto = reportDataQuery.getAiReasoningReportDto(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting simulation AI Reasoning report from storage, fileStorageName={}",
        fileStorageName);

    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadAiReasoningReportDto.builder()
        .name(getFileName(dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(AiReasoningReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private ReportFileNameDto toReportFileNameDto(AiReasoningReportDto dto) {
    ReportRange range = dto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(dateFormatter.format(range.getFrom()))
        .to(dateFormatter.format(range.getTo()))
        .timestamp(dto.getTimestamp())
        .build();
  }
}
