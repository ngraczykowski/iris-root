package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;

import static java.util.Collections.singleton;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AddRolesToUserUseCaseFixtures {

  static final AddRolesToUserCommand ADD_ANALYST_ROLE_COMMAND =
      AddRolesToUserCommand
          .builder()
          .username("jsmith")
          .roles(singleton("Analyst"))
          .build();
}
