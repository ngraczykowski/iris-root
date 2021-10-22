package com.silenteight.warehouse.report.reasoning.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.v1.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.v1.download.DeprecatedAiReasoningReportDataQuery;
import com.silenteight.warehouse.report.reasoning.v1.status.DeprecatedAiReasoningReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class DeprecatedAiReasoningReportQuery
    implements DeprecatedAiReasoningReportDataQuery, DeprecatedAiReasoningReportStatusQuery {

  @NonNull
  private final DeprecatedAiReasoningReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    return ofNullable(repository.getById(id))
        .map(DeprecatedAiReasoningReport::getFileStorageName)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(DeprecatedAiReasoningReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }
}
