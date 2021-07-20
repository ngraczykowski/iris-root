package com.silenteight.warehouse.report.sm.download;

import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

public interface SimulationMetricsReportDataQuery {

  ReportDto getReport(long id);
}
