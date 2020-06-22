package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.ReasoningBranchValidator;

import io.vavr.control.Try;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.REASONING_BRANCH;

@RequiredArgsConstructor
@Slf4j
public class UpdateReasoningBranchesUseCase {

  private static final Try<Void> NO_CHANGES = Try.success(null);

  private final ChangeRequestRepository repository;

  private final ReasoningBranchValidator reasoningBranchValidator;

  private final AuditTracer auditTracer;

  public Try<Void> apply(UpdateBranchesCommand updateCommand) {
    log.info(REASONING_BRANCH, "Updating Reasoning Branches details. command={}", updateCommand);

    if (updateCommand.doesNotHaveChanges()) {
      log.info(REASONING_BRANCH, "No changes detected in Reasoning Branches update data.");
      return NO_CHANGES;
    }

    reasoningBranchValidator.validateIds(updateCommand.getTreeId(), updateCommand.getBranchIds());

    auditTracer.save(new ReasoningBranchUpdateRequestedEvent(updateCommand));

    return repository.save(updateCommand)
        .onSuccess(ignored -> log.info(REASONING_BRANCH, "Reasoning Branches update applied."))
        .onFailure(reason -> log.error(REASONING_BRANCH, "Could not apply update", reason));
  }
}
