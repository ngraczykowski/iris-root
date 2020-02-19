package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserDisplayNameUseCase {

  private final UpdatedUserRepository updatedUserRepository;

  public void apply(UpdateUserDisplayNameCommand command) {
    log.debug("Updating user. command={}", command);

    updatedUserRepository.save(command.toUpdatedUser());
  }

  @Data
  @Builder
  public static class UpdateUserDisplayNameCommand {

    @NonNull
    private final String username;
    private final String displayName;

    UpdatedUser toUpdatedUser() {
      return UpdatedUser
          .builder()
          .username(username)
          .displayName(displayName)
          .build();
    }
  }
}
