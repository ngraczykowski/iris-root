package com.silenteight.warehouse.report.accuracy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;
import com.silenteight.warehouse.report.accuracy.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.accuracy.download.AccuracyReportDataQuery;
import com.silenteight.warehouse.report.accuracy.status.AccuracyReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AccuracyReportQuery implements AccuracyReportDataQuery, AccuracyReportStatusQuery {

  @NonNull
  private final AccuracyReportRepository repository;

  @Override
  public AccuracyReportDto getAccuracyReportDto(long id) {
    return ofNullable(repository.getById(id))
        .map(AccuracyReportQuery::toAccuracyReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static AccuracyReportDto toAccuracyReportDto(AccuracyReport report) {
    return AccuracyReportDto.builder()
        .fileStorageName(report.getFileStorageName())
        .range(toReportRange(report))
        .timestamp(toTimestamp(report.getCreatedAt()))
        .build();
  }

  private static ReportRange toReportRange(AccuracyReport report) {
    return of(report.getFrom(), report.getTo());
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(AccuracyReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
