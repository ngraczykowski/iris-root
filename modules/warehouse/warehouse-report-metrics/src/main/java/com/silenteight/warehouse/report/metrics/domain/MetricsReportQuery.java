package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.metrics.download.MetricsReportDataQuery;
import com.silenteight.warehouse.report.metrics.status.MetricsReportStatusQuery;

@RequiredArgsConstructor
class MetricsReportQuery implements MetricsReportDataQuery, MetricsReportStatusQuery {

  @NonNull
  private final MetricsReportRepository repository;

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }
}
