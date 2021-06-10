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
import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserDisplayNameUseCase {

  @NonNull
  private final UpdatedUserRepository updatedUserRepository;
  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final UserRolesRetriever userRolesRetriever;
  @NonNull
  private final String rolesScope;

  public void apply(UpdateUserDisplayNameCommand command) {
    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), UpdateUserDisplayNameCommand.class.getName(), command));
    UpdatedUser updatedUser = command.toUpdatedUser();

    updatedUserRepository.save(updatedUser);

    auditTracer.save(new UserUpdatedEvent(
        updatedUser.getUsername(),
        UpdatedUser.class.getName(),
        new UpdatedUserDetails(
            updatedUser,
            ofNullable(updatedUser.getRoles())
                .map(userRoles -> userRoles.getRoles(rolesScope))
                .orElse(null),
            userRolesRetriever.rolesOf(updatedUser.getUsername()).getRoles(rolesScope))));
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
      return UpdatedUser.builder()
          .username(username)
          .displayName(displayName)
          .updateDate(timeSource.offsetDateTime())
          .build();
    }
  }
}
