package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.metrics.download.MetricsReportDataQuery;
import com.silenteight.warehouse.report.metrics.status.MetricsReportStatusQuery;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class MetricsReportQuery implements MetricsReportDataQuery, MetricsReportStatusQuery {

  @NonNull
  private final MetricsReportRepository repository;

  @Override
  public ReportDto getReport(long id) {
    return ofNullable(repository.getById(id))
        .map(MetricsReport::toDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(MetricsReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
