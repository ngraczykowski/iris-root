package com.silenteight.warehouse.report.billing.v1.status;

import com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState;

public interface DeprecatedReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
