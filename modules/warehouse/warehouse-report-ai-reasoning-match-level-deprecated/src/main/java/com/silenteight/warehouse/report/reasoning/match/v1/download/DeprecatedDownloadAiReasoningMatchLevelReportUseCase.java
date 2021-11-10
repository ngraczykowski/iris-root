package com.silenteight.warehouse.report.reasoning.match.v1.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedDownloadAiReasoningMatchLevelReportUseCase {

  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;

  public FileDto activate(long id) {
    log.debug("Getting AI Reasoning Match Level report, reportId={}", id);
    String fileName = reportDataQuery.getReportFileName(id);
    log.debug("Getting AI Reasoning Match Level report from storage, fileName={}", fileName);
    return reportStorageService.getReport(fileName);
  }
}
