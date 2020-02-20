package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.vavr.control.Try;

@RequiredArgsConstructor
@Slf4j
public class UpdateReasoningBranchUseCase {

  private static final Try<Void> NO_CHANGES = Try.success(null);

  private final ReasoningBranchUpdateRepository repository;

  public Try<Void> apply(UpdateBranchCommand updateBranchCommand) {
    log.debug("Update reasoning branch use case. branchId={}", updateBranchCommand.getBranchId());

    if (updateBranchCommand.doesNotHaveChanges())
      return NO_CHANGES;

    return repository.save(updateBranchCommand);
  }
}
