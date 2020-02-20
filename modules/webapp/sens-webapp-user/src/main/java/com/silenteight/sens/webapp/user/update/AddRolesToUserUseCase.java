package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class AddRolesToUserUseCase {

  private final UpdatedUserRepository updatedUserRepository;

  public void apply(AddRolesToUserCommand command) {
    log.debug("Adding roles to user. command={}", command);

    updatedUserRepository.save(command.toUpdatedUser());
  }

  @Data
  @Builder
  public static class AddRolesToUserCommand {

    @NonNull
    private final String username;
    @NonNull
    private final Set<String> roles;

    UpdatedUser toUpdatedUser() {
      return UpdatedUser
          .builder()
          .username(username)
          .roles(roles)
          .build();
    }
  }
}
