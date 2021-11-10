package com.silenteight.warehouse.report.reasoning.match.v1.status;

import com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState;

public interface DeprecatedAiReasoningMatchLevelReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
