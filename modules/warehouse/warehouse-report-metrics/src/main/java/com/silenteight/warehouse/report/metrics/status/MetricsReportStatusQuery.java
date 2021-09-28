package com.silenteight.warehouse.report.metrics.status;

import com.silenteight.warehouse.report.metrics.domain.ReportState;

public interface MetricsReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
