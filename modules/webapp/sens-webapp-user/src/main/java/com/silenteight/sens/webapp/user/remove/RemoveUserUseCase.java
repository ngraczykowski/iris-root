package com.silenteight.sens.webapp.user.remove;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.UserRemover;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.Collection;

import static java.util.Set.of;

@Slf4j
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
    log.debug("Removing user with username={}", username);

    auditTracer.save(
        new UserRemovalRequestedEvent(
            username, RemoveUserCommand.class.getName(), command));
    verifyOrigin(username, command.expectedOrigin);
    Collection<String> roles = userRolesRetriever.rolesOf(username).getRoles(rolesScope);
    userRemover.remove(username);
    auditTracer.save(
        new UserRemovedEvent(
            username, RemoveUserCommand.class.getName(), new RemovedUserDetails(command, roles)));
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
