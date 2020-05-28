package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserDisplayNameUseCase {

  @NonNull
  private final UpdatedUserRepository updatedUserRepository;
  @NonNull
  private final AuditTracer auditTracer;

  public void apply(UpdateUserDisplayNameCommand command) {
    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), UpdateUserDisplayNameCommand.class.getName(), command));

    updatedUserRepository.save(command.toUpdatedUser());
  }

  @Data
  @Builder
  public static class UpdateUserDisplayNameCommand {

    @NonNull
    private final String username;
    @NonNull
    private final String displayName;
    @Default
    @NonNull
    private final TimeSource timeSource = DefaultTimeSource.INSTANCE;

    UpdatedUser toUpdatedUser() {
      return UpdatedUser
          .builder()
          .username(username)
          .displayName(displayName)
          .updateDate(timeSource.offsetDateTime())
          .build();
    }
  }
}
