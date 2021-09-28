package com.silenteight.warehouse.report.accuracy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.accuracy.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.accuracy.download.AccuracyReportDataQuery;
import com.silenteight.warehouse.report.accuracy.status.AccuracyReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AccuracyReportQuery implements AccuracyReportDataQuery, AccuracyReportStatusQuery {

  @NonNull
  private final AccuracyReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    AccuracyReport accuracyReport = ofNullable(repository.getById(id))
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));

    return accuracyReport.getFile();
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }
}
