package com.silenteight.warehouse.report.sm.state;

import com.silenteight.warehouse.report.sm.domain.ReportState;

public interface SimulationMetricsReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
