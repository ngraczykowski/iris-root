package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import io.vavr.control.Try;

public interface ReasoningBranchUpdateRepository {

  Try<Void> save(UpdatedBranch updatedBranch);
}
