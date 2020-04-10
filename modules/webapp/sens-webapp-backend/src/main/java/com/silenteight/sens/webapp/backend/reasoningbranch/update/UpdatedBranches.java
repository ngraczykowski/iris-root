package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import java.util.List;
import java.util.Optional;

public interface UpdatedBranches {

  Optional<String> getNewAiSolution();

  Optional<Boolean> getNewStatus();

  List<BranchId> getBranchIds();
}
