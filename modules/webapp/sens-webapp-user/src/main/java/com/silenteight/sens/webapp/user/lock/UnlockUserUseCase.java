package com.silenteight.sens.webapp.user.lock;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

@RequiredArgsConstructor
public class UnlockUserUseCase {

  @NonNull
  private final UserLocker userLocker;
  @NonNull
  private final AuditTracer auditTracer;

  public void apply(UnlockUserCommand command) {
    auditTracer.save(
        new UserUnlockRequestedEvent(
            command.getUsername(), UnlockUserCommand.class.getName(), command));

    userLocker.unlock(command.getUsername());

    auditTracer.save(
        new UserUnlockedEvent(command.getUsername(), UnlockUserCommand.class.getName(), command));
  }

  @Value
  @Builder
  public static class UnlockUserCommand {

    @NonNull
    private final String username;
  }
}
