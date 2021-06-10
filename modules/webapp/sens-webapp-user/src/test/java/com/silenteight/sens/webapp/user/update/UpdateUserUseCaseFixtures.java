package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sens.webapp.user.update.UpdateUserUseCase.UpdateUserCommand;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.usermanagement.api.UpdatedUser;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.util.Set.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UpdateUserUseCaseFixtures {

  private static final MockTimeSource MOCK_TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;

  static final OffsetDateTime OFFSET_DATE_TIME = MOCK_TIME_SOURCE.offsetDateTime();
  static final Set<String> ROLES = of("Analyst", "Auditor");

  private static final String USERNAME = "jsmith";
  private static final String DISPLAY_NAME = "John Smith";
  private static final String ROLES_SCOPE = "frontend";
  private static final Boolean LOCKED = FALSE;

  static final UpdateUserCommand UPDATE_USER_COMMAND =
      UpdateUserCommand.builder()
          .username(USERNAME)
          .displayName(DISPLAY_NAME)
          .roles(ROLES)
          .locked(LOCKED)
          .timeSource(MOCK_TIME_SOURCE)
          .build();

  static final UpdatedUser UPDATED_USER =
      UpdatedUser.builder()
          .username(USERNAME)
          .displayName(DISPLAY_NAME)
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, new ArrayList<>(ROLES))))
          .locked(LOCKED)
          .updateDate(OFFSET_DATE_TIME)
          .build();
}
