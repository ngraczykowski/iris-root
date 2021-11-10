package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.v1.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.match.v1.download.DeprecatedAiReasoningMatchLevelReportDataQuery;
import com.silenteight.warehouse.report.reasoning.match.v1.status.DeprecatedAiReasoningMatchLevelReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class DeprecatedAiReasoningMatchLevelReportQuery
    implements DeprecatedAiReasoningMatchLevelReportDataQuery,
    DeprecatedAiReasoningMatchLevelReportStatusQuery {

  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    return ofNullable(repository.getById(id))
        .map(DeprecatedAiReasoningMatchLevelReport::getFileStorageName)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(DeprecatedAiReasoningMatchLevelReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }
}
