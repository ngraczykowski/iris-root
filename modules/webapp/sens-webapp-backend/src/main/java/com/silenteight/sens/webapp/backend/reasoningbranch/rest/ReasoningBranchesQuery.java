package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import java.util.List;

public interface ReasoningBranchesQuery {

  List<BranchDto> findByTreeIdAndBranchIds(long treeId, List<Long> branchIds);
}
