package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import lombok.Value;

import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.report.Report;

@Value
class ReasoningBranchReport implements Report {

  String reportFileName;

  LinesSupplier reportContent;
}
