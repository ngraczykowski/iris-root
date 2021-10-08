package com.silenteight.warehouse.report.reasoning.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
public class DownloadAiReasoningReportUseCase {

  @NonNull
  private final AiReasoningReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public FileDto activate(long id) {
    log.debug("Getting AI Reasoning report, reportId={}", id);
    String fileName = reportDataQuery.getReportFileName(id);
    log.debug("Getting AI Reasoning report from storage, fileName={}", fileName);
    return reportStorageService.getReport(fileName);
  }
}
