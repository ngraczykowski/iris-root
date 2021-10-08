package com.silenteight.warehouse.report.reasoning.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.download.AiReasoningReportDataQuery;
import com.silenteight.warehouse.report.reasoning.status.AiReasoningReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AiReasoningReportQuery implements AiReasoningReportDataQuery, AiReasoningReportStatusQuery {

  @NonNull
  private final AiReasoningReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningReport::getFile)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));
  }
}
