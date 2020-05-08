package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import java.util.List;
import java.util.Optional;

public interface UpdatedBranches {

  Optional<String> getNewAiSolution();

  Optional<Boolean> getNewStatus();

  Optional<String> getComment();

  List<Long> getBranchIds();

  long getTreeId();
}
