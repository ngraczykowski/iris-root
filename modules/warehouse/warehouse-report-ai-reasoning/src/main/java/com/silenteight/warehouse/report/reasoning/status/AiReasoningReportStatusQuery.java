package com.silenteight.warehouse.report.reasoning.status;

import com.silenteight.warehouse.report.reasoning.domain.ReportState;

public interface AiReasoningReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
