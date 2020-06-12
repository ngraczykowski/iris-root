package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.report;

import java.util.List;

public interface ReasoningBranchesReportQuery {

  List<BranchWithFeaturesDto> findByTreeId(long treeId);
}
