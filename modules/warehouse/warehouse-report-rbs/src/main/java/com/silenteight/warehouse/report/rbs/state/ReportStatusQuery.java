package com.silenteight.warehouse.report.rbs.state;

import com.silenteight.warehouse.report.rbs.domain.ReportState;

public interface ReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
