package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
@Slf4j
public class AddRolesToUserUseCase {

  @NonNull
  private final UpdatedUserRepository updatedUserRepository;
  @NonNull
  private final UserQuery userQuery;
  @NonNull
  private final AuditTracer auditTracer;

  public void apply(AddRolesToUserCommand command) {
    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), AddRolesToUserCommand.class.getName(), command));

    userQuery
        .find(command.getUsername())
        .map(UserDto::getRoles)
        .map(command::toUpdatedUser)
        .ifPresentOrElse(
            updatedUserRepository::save,
            () -> log.warn("Could not find user. username={}", command.getUsername()));
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

    UpdatedUser toUpdatedUser(Collection<String> userRoles) {
      return UpdatedUser
          .builder()
          .username(username)
          .roles(concat(userRoles.stream(), rolesToAdd.stream()).collect(toSet()))
          .updateDate(timeSource.offsetDateTime())
          .build();
    }
  }
}
