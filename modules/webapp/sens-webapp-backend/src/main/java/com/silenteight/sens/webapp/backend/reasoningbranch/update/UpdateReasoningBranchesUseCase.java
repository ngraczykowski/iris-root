package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import io.vavr.control.Try;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.REASONING_BRANCH;


@RequiredArgsConstructor
public class UpdateReasoningBranchesUseCase {

  private static final Try<Void> NO_CHANGES = Try.success(null);

  private final ChangeRequestRepository repository;
  private final AuditLog auditLog;

  public Try<Void> apply(UpdateBranchesCommand updateCommand) {
    auditLog.logInfo(
        REASONING_BRANCH, "Updating Reasoning Branches details. command={}", updateCommand);

    if (updateCommand.doesNotHaveChanges()) {
      auditLog.logInfo(REASONING_BRANCH, "No changes detected in Reasoning Branches update data.");
      return NO_CHANGES;
    }

    return repository.save(updateCommand)
        .onSuccess(
            ignored -> auditLog.logInfo(REASONING_BRANCH, "Reasoning Branches update applied."))
        .onFailure(reason -> auditLog.logError(REASONING_BRANCH, "Could not apply update", reason));
  }
}
