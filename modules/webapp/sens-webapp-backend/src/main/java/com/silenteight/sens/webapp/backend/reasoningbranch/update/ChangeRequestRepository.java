package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import io.vavr.control.Try;

public interface ChangeRequestRepository {

  Try<Void> save(UpdatedBranches updateBranches);
}
