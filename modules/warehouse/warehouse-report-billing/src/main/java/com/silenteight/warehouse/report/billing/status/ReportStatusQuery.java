package com.silenteight.warehouse.report.billing.status;

import com.silenteight.warehouse.report.billing.domain.ReportState;

public interface ReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
