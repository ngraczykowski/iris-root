package com.silenteight.warehouse.report.reasoning.match.status;

import com.silenteight.warehouse.report.reasoning.match.domain.ReportState;

public interface AiReasoningMatchLevelReportStatusQuery {

  ReportState getReportGeneratingState(long id);
}
