package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;
import com.silenteight.warehouse.report.rbs.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.rbs.download.RbsReportDataQuery;
import com.silenteight.warehouse.report.rbs.status.RbsReportStatusQuery;

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
  public ReportDto getReport(long id) {
    return ofNullable(repository.getById(id))
        .map(RbsReport::toDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
