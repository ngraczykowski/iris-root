package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;
import com.silenteight.warehouse.report.billing.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.billing.download.ReportDataQuery;
import com.silenteight.warehouse.report.billing.status.ReportStatusQuery;

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
  public ReportDto getReport(long id) {
    return ofNullable(repository.getById(id))
        .map(BillingReport::toDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
