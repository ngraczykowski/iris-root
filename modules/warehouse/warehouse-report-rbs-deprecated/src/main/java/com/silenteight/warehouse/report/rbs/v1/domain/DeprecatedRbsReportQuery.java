package com.silenteight.warehouse.report.rbs.v1.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.v1.domain.dto.ReportDto;
import com.silenteight.warehouse.report.rbs.v1.download.DeprecatedRbsReportDataQuery;
import com.silenteight.warehouse.report.rbs.v1.status.DeprecatedRbsReportStatusQuery;

@RequiredArgsConstructor
class DeprecatedRbsReportQuery
    implements DeprecatedRbsReportStatusQuery, DeprecatedRbsReportDataQuery {

  @NonNull
  private final DeprecatedRbsReportRepository repository;

  @Override
  public DeprecatedReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }
}
