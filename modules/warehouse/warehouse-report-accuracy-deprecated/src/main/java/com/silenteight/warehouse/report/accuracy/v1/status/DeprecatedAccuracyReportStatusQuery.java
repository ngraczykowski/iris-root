package com.silenteight.warehouse.report.accuracy.v1.status;

import com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedReportState;

public interface DeprecatedAccuracyReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
