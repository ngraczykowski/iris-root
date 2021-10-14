package com.silenteight.warehouse.report.metrics.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.metrics.v1.download.DeprecatedMetricsReportDataQuery;
import com.silenteight.warehouse.report.metrics.v1.status.DeprecatedMetricsReportStatusQuery;

@RequiredArgsConstructor
class DeprecatedMetricsReportQuery
    implements DeprecatedMetricsReportDataQuery, DeprecatedMetricsReportStatusQuery {

  @NonNull
  private final DeprecatedMetricsReportRepository repository;

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }
}
