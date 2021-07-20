package com.silenteight.warehouse.report.sm.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;
import com.silenteight.warehouse.report.sm.download.SimulationMetricsReportDataQuery;
import com.silenteight.warehouse.report.sm.state.SimulationMetricsReportStatusQuery;

@RequiredArgsConstructor
class SimulationMetricsReportQuery implements
    SimulationMetricsReportDataQuery,
    SimulationMetricsReportStatusQuery {

  @NonNull
  private final SimulationMetricsReportRepository repository;

  @Override
  public ReportDto getReport(long id) {
    return repository.getById(id).toDto();
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return repository.getById(id).getState();
  }
}
