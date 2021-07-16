package com.silenteight.warehouse.report.rbs.status;

import com.silenteight.warehouse.report.rbs.domain.ReportState;

public interface RbsReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
