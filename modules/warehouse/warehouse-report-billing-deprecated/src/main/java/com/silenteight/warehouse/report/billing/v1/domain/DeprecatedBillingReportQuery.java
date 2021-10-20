package com.silenteight.warehouse.report.billing.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.billing.v1.download.DeprecatedReportDataQuery;
import com.silenteight.warehouse.report.billing.v1.status.DeprecatedReportStatusQuery;

@RequiredArgsConstructor
class DeprecatedBillingReportQuery
    implements DeprecatedReportStatusQuery, DeprecatedReportDataQuery {

  @NonNull
  private final DeprecatedBillingReportRepository repository;

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }
}
