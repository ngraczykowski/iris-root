package com.silenteight.warehouse.report.rbs.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.SIMULATION;

@RequiredArgsConstructor
public class CreateRbsReportUseCase {

  private final RbsReportService reportService;

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    return reportService.createProductionReportInstance(ReportDefinition.getReportType(reportId));
  }

  ReportInstanceReferenceDto createSimulationReport(String analysisId) {
    return reportService.createSimulationReportInstance(analysisId, SIMULATION);
  }
}
