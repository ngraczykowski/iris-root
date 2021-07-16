package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;
import com.silenteight.warehouse.report.rbs.download.RbsReportDataQuery;
import com.silenteight.warehouse.report.rbs.status.RbsReportStatusQuery;

@RequiredArgsConstructor
class RbsReportQuery implements RbsReportStatusQuery, RbsReportDataQuery {

  @NonNull
  private final RbsReportRepository repository;

  @Override
  public ReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }
}
