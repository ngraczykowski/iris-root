package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import java.util.Optional;

public interface ReasoningBranchDetailsQuery {

  Optional<BranchDetailsDto> findByTreeIdAndBranchId(long treeId, long branchId);
}
