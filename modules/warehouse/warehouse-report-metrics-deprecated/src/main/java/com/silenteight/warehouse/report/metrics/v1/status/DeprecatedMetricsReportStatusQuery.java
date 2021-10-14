package com.silenteight.warehouse.report.metrics.v1.status;

import com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedReportState;

public interface DeprecatedMetricsReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
