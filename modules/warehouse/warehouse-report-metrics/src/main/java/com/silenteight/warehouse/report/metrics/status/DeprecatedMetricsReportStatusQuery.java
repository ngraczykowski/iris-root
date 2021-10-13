package com.silenteight.warehouse.report.metrics.status;

import com.silenteight.warehouse.report.metrics.domain.DeprecatedReportState;

public interface DeprecatedMetricsReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
