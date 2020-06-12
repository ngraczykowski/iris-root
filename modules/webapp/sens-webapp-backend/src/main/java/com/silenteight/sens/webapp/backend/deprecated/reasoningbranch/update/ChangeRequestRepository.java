package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update;

import io.vavr.control.Try;

public interface ChangeRequestRepository {

  Try<Void> save(UpdatedBranches updatedBranches);
}
