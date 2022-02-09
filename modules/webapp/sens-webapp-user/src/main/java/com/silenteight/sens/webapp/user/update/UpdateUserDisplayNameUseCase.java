package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.user.UserUpdater;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;


import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserDisplayNameUseCase {

  @NonNull
  private final UserUpdater userUpdater;
  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final UserRolesRetriever userRolesRetriever;
  @NonNull
  private final String rolesScope;

  public void apply(UpdateUserDisplayNameCommand command) {
    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), UpdateUserDisplayNameCommand.class.getName(), command));
    UpdateUserCommand updateUserCommand = command.toUpdatedUser();

    userUpdater.update(updateUserCommand);

    auditTracer.save(new UserUpdatedEvent(
        updateUserCommand.getUsername(),
        UpdateUserCommand.class.getName(),
        new UpdatedUserDetails(
            updateUserCommand,
            ofNullable(updateUserCommand.getRoles())
                .map(userRoles -> userRoles.getRoles(rolesScope))
                .orElse(null),
            userRolesRetriever.rolesOf(updateUserCommand.getUsername()).getRoles(rolesScope))));
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

    UpdateUserCommand toUpdatedUser() {
      return UpdateUserCommand.builder()
          .username(username)
          .displayName(displayName)
          .updateDate(timeSource.offsetDateTime())
          .build();
    }
  }
}
