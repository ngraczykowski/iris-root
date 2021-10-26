package com.silenteight.warehouse.report.accuracy.status;

import com.silenteight.warehouse.report.accuracy.domain.ReportState;

public interface AccuracyReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
