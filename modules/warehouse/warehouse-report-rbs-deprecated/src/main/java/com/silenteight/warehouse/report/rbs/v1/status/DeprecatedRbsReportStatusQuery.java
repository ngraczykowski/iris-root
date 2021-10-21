package com.silenteight.warehouse.report.rbs.v1.status;

import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportState;

public interface DeprecatedRbsReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
