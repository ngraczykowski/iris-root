package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.domain.dto.BillingReportDto;
import com.silenteight.warehouse.report.billing.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.billing.download.ReportDataQuery;
import com.silenteight.warehouse.report.billing.status.ReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class BillingReportQuery implements ReportStatusQuery, ReportDataQuery {

  @NonNull
  private final BillingReportRepository repository;

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(BillingReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  @Override
  public BillingReportDto getReport(long id) {
    return ofNullable(repository.getById(id))
        .map(BillingReportQuery::toBillingReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static BillingReportDto toBillingReportDto(BillingReport report) {
    return BillingReportDto.builder()
        .content(report.getData())
        .range(toReportRange(report))
        .timestamp(toTimestamp(report.getCreatedAt()))
        .build();
  }

  private static ReportRange toReportRange(BillingReport report) {
    return of(report.getFrom(), report.getTo());
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }
}
