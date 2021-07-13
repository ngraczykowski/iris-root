package com.silenteight.warehouse.report.rbs.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

@RequiredArgsConstructor
public class CreateReportUseCase {

  private final RbsReportService reportService;

  ReportInstanceReferenceDto createProductionReport(String reportName) {
    ReportDefinition reportType = ReportDefinition.getReportType(reportName);
    return reportService.createReportInstance(reportType);
  }
}
