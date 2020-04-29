package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import lombok.Value;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;

@Value
class ReasoningBranchReport implements Report {
  String reportFileName;

  LinesSupplier reportContent;
}
