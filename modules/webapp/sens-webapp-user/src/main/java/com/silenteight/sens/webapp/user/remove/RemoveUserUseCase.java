package com.silenteight.sens.webapp.user.remove;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.UserRemover;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import static java.util.Set.of;

@RequiredArgsConstructor
public class RemoveUserUseCase {

  @NonNull
  private final UserQuery userQuery;
  @NonNull
  private final UserRemover userRemover;
  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final UserRolesRetriever userRolesRetriever;
  @NonNull
  private final String rolesScope;
  @NonNull
  private final String countryGroupsScope;

  public void apply(RemoveUserCommand command) {
    String username = command.getUsername();

    auditTracer.save(
        new UserRemovalRequestedEvent(
            username, RemoveUserCommand.class.getName(), command));
    verifyOrigin(username, command.expectedOrigin);
    userRemover.remove(username);
    auditTracer.save(
        new UserRemovedEvent(
            username, 
            RemoveUserCommand.class.getName(),
            new RemovedUserDetails(
                command, userRolesRetriever.rolesOf(username).getRoles(rolesScope))));
  }

  private void verifyOrigin(String username, String expectedOrigin) {
    UserDto user = userQuery
        .find(username, of(rolesScope, countryGroupsScope))
        .orElseThrow(() -> new UserNotFoundException());

    if (!containsOrigin(user, expectedOrigin))
      throw new OriginNotMatchingException();
  }

  private boolean containsOrigin(UserDto user, String origin) {
    return user.getOrigin().equals(origin);
  }

  @Value
  @Builder
  public static class RemoveUserCommand {

    @NonNull
    private final String username;

    @NonNull
    private final String expectedOrigin;
  }
}
