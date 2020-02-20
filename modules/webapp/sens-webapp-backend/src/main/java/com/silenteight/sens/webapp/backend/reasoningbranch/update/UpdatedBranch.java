package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import java.util.Optional;

public interface UpdatedBranch {

  BranchId getBranchId();

  Optional<String> getNewAiSolution();

  Optional<Boolean> getNewIsActive();

  default boolean doesNotHaveChanges() {
    return getNewAiSolution().isEmpty() && getNewIsActive().isEmpty();
  }
}
