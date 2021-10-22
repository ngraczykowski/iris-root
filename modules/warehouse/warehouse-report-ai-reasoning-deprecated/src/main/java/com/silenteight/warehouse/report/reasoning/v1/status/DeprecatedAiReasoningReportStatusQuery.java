package com.silenteight.warehouse.report.reasoning.v1.status;

import com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedReportState;

public interface DeprecatedAiReasoningReportStatusQuery {

  DeprecatedReportState getReportGeneratingState(long id);
}
