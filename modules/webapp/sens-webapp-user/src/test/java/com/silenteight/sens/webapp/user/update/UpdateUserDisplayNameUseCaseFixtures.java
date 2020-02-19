package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.update.UpdateUserDisplayNameUseCase.UpdateUserDisplayNameCommand;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UpdateUserDisplayNameUseCaseFixtures {

  static final UpdateUserDisplayNameCommand NEW_DISPLAY_NAME_COMMAND =
      UpdateUserDisplayNameCommand
          .builder()
          .username("jsmith")
          .displayName("John Smith")
          .build();
}
