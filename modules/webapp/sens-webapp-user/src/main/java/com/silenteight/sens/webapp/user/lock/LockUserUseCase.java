package com.silenteight.sens.webapp.user.lock;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.UserLocker;

@RequiredArgsConstructor
public class LockUserUseCase {

  @NonNull
  private final UserLocker userLocker;
  @NonNull
  private final AuditTracer auditTracer;

  public void apply(LockUserCommand command) {
    auditTracer.save(
        new UserLockRequestedEvent(
            command.getUsername(), LockUserCommand.class.getName(), command));

    userLocker.lock(command.getUsername());

    auditTracer.save(
        new UserLockedEvent(command.getUsername(), LockUserCommand.class.getName(), command));
  }

  @Value
  @Builder
  public static class LockUserCommand {

    @NonNull
    private final String username;
  }
}
