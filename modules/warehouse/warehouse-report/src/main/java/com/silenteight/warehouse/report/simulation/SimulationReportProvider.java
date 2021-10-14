package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

public interface SimulationReportProvider {

  ReportTypeDto getReport(String analysisId);
}
