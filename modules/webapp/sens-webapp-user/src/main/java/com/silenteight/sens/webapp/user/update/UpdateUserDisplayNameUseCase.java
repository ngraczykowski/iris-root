package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.common.time.TimeSource;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserDisplayNameUseCase {

  private final UpdatedUserRepository updatedUserRepository;

  public void apply(UpdateUserDisplayNameCommand command) {
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
