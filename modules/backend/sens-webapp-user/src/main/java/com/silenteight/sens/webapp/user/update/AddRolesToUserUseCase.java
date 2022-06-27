package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.UserUpdater;
import com.silenteight.sep.usermanagement.api.user.dto.UpdateUserCommand;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Set.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
@Slf4j
public class AddRolesToUserUseCase {

  @NonNull
  private final UserUpdater userUpdater;
  @NonNull
  private final UserQuery userQuery;
  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final String rolesScope;
  @NonNull
  private final String countryGroupsScope;

  public void apply(AddRolesToUserCommand command) {
    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), AddRolesToUserCommand.class.getName(), command));

    userQuery
        .find(command.getUsername(), of(rolesScope, countryGroupsScope))
        .map(UserDto::getRoles)
        .map(roles -> command.toUpdatedUser(rolesScope, roles))
        .ifPresentOrElse(
            this::saveUser,
            () -> log.warn("Could not find user. username={}", command.getUsername()));
  }

  private void saveUser(UpdateUserCommand user) {
    userUpdater.update(user);
    auditTracer.save(new UserUpdatedEvent(user.getUsername(), UpdateUserCommand.class.getName(),
        new UpdatedUserDetails(
            user, user.getRoles().getRoles(rolesScope), emptyList())));
  }

  @Data
  @Builder
  public static class AddRolesToUserCommand {

    @NonNull
    private final String username;
    @NonNull
    private final Set<String> rolesToAdd;
    @NonNull
    @Default
    private final TimeSource timeSource = DefaultTimeSource.INSTANCE;

    UpdateUserCommand toUpdatedUser(String rolesScope, UserRoles roles) {
      Map<String, List<String>> rolesByScope = toRolesByScope(roles);
      rolesByScope.put(rolesScope, toNewRoles(rolesScope, roles));
      UserRoles newUserRoles = new ScopeUserRoles(rolesByScope);

      return UpdateUserCommand.builder()
          .username(username)
          .roles(newUserRoles)
          .updateDate(timeSource.offsetDateTime())
          .build();
    }

    private static Map<String, List<String>> toRolesByScope(UserRoles roles) {
      Map<String, List<String>> result = new HashMap<>();
      roles
          .getScopes()
          .forEach(scope -> result.put(scope, roles.getSortedRoles(scope)));

      return result;
    }

    private List<String> toNewRoles(String rolesScope, UserRoles roles) {
      List<String> currentRoles = roles.getSortedRoles(rolesScope);
      return concat(currentRoles.stream(), rolesToAdd.stream()).collect(toList());
    }
  }
}
