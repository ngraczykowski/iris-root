package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;
import com.silenteight.warehouse.report.billing.download.ReportDataQuery;
import com.silenteight.warehouse.report.billing.status.ReportStatusQuery;

@RequiredArgsConstructor
class BillingReportQuery implements ReportStatusQuery, ReportDataQuery {

  @NonNull
  private final BillingReportRepository repository;

  @Override
  public ReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }
}
