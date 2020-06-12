package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import java.util.List;

public interface ReasoningBranchesQuery {

  List<BranchDto> findBranchByTreeIdAndBranchIds(long treeId, List<Long> branchIds);
}
