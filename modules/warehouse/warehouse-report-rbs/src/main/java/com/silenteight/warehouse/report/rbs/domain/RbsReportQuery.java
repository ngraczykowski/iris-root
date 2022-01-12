package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;
import com.silenteight.warehouse.report.rbs.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.rbs.download.RbsReportDataQuery;
import com.silenteight.warehouse.report.rbs.status.RbsReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class RbsReportQuery implements RbsReportStatusQuery, RbsReportDataQuery {

  @NonNull
  private final RbsReportRepository repository;

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(RbsReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  @Override
  public RbsReportDto getRbsReport(long id) {
    return ofNullable(repository.getById(id))
        .map(RbsReportQuery::toRbsReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static RbsReportDto toRbsReportDto(RbsReport report) {
    return RbsReportDto.builder()
        .fileName(report.getFileName())
        .range(toReportRange(report))
        .timestamp(toTimestamp(report.getCreatedAt()))
        .build();
  }

  private static ReportRange toReportRange(RbsReport report) {
    return of(report.getFrom(), report.getTo());
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }
}
