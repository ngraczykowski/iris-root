package com.silenteight.warehouse.report.rbs.v1.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedRbsReportService;
import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import static com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportDefinition.SIMULATION;

@RequiredArgsConstructor
public class DeprecatedCreateRbsReportUseCase {

  private final DeprecatedRbsReportService reportService;

  ReportInstanceReferenceDto createProductionReport(String reportId) {

    return reportService.createProductionReportInstance(
        DeprecatedReportDefinition.getReportType(reportId));
  }

  ReportInstanceReferenceDto createSimulationReport(String analysisId) {
    return reportService.createSimulationReportInstance(analysisId, SIMULATION);
  }
}
