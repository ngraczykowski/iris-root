package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.vavr.control.Try;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;

@RequiredArgsConstructor
@Slf4j
public class UpdateReasoningBranchUseCase {

  private static final Try<Void> NO_CHANGES = Try.success(null);

  private final ReasoningBranchUpdateRepository repository;

  public Try<Void> apply(UpdateBranchCommand updateCommand) {
    log.debug(REASONING_BRANCH, "Updating Reasoning Branch details. command={}", updateCommand);

    if (updateCommand.doesNotHaveChanges()) {
      log.debug(REASONING_BRANCH, "No changes detected in Reasoning Branch update data.");
      return NO_CHANGES;
    }

    return repository.save(updateCommand)
        .onSuccess(ignored -> log.debug(REASONING_BRANCH, "Reasoning Branch update applied."))
        .onFailure(reason -> log.error(REASONING_BRANCH, "Could not apply update", reason));
  }
}
