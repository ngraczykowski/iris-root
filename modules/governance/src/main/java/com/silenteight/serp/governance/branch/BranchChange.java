package com.silenteight.serp.governance.branch;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.Optional;

interface BranchChange {

  Optional<BranchSolution> getSolutionChange();

  Optional<Boolean> getEnabledChange();
}
