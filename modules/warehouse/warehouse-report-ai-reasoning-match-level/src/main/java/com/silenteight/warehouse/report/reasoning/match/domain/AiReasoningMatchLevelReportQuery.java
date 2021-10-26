package com.silenteight.warehouse.report.reasoning.match.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.match.download.AiReasoningMatchLevelReportDataQuery;
import com.silenteight.warehouse.report.reasoning.match.status.AiReasoningMatchLevelReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AiReasoningMatchLevelReportQuery
    implements AiReasoningMatchLevelReportDataQuery, AiReasoningMatchLevelReportStatusQuery {

  @NonNull
  private final AiReasoningMatchLevelReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningMatchLevelReport::getFileStorageName)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningMatchLevelReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }
}
