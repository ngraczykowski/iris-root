package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.dto.MetricsReportDto;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.metrics.download.MetricsReportDataQuery;
import com.silenteight.warehouse.report.metrics.status.MetricsReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class MetricsReportQuery implements MetricsReportDataQuery, MetricsReportStatusQuery {

  @NonNull
  private final MetricsReportRepository repository;

  @Override
  public MetricsReportDto getReport(long id) {
    return ofNullable(repository.getById(id))
        .map(MetricsReportQuery::toMetricsReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static MetricsReportDto toMetricsReportDto(MetricsReport report) {
    return MetricsReportDto.builder()
        .content(report.getData())
        .range(toReportRange(report))
        .timestamp(toTimestamp(report.getCreatedAt()))
        .build();
  }

  private static ReportRange toReportRange(MetricsReport report) {
    return of(report.getFrom(), report.getTo());
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }


  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(MetricsReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
