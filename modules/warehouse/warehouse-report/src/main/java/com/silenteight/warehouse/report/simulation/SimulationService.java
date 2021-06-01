package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.report.reporting.ReportingService;

@RequiredArgsConstructor
public class SimulationService {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final TimeSource timeSource;

  public ReportInstance createSimulationReport(String analysisId, String definitionId) {
    String tenantName = reportingService.getTenantIdByAnalysisId(analysisId);

    long timestamp = timeSource.now().toEpochMilli();
    reportingService.createReport(definitionId, tenantName);

    return new ReportInstance(timestamp);
  }
}
