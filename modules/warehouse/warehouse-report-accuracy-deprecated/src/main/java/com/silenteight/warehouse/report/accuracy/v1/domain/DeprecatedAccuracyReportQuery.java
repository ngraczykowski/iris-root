package com.silenteight.warehouse.report.accuracy.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.accuracy.v1.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.accuracy.v1.download.DeprecatedAccuracyReportDataQuery;
import com.silenteight.warehouse.report.accuracy.v1.status.DeprecatedAccuracyReportStatusQuery;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class DeprecatedAccuracyReportQuery implements DeprecatedAccuracyReportDataQuery,
    DeprecatedAccuracyReportStatusQuery {

  @NonNull
  private final DeprecatedAccuracyReportRepository repository;

  @Override
  public String getReportFileName(long id) {
    DeprecatedAccuracyReport accuracyReport = ofNullable(repository.getById(id))
        .orElseThrow(() -> new ReportNotFoundException(valueOf(id)));

    return accuracyReport.getFileStorageName();
  }

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }
}
