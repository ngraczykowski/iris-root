package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AddRolesToUserUseCaseFixtures {

  static final String ROLES_SCOPE = "frontend";
  static final String COUNTRY_GROUPS_SCOPE = "kibana";

  private static final String USERNAME = "jsmith";
  private static final MockTimeSource MOCK_TIME_SOURCE = MockTimeSource.ARBITRARY_INSTANCE;
  static final OffsetDateTime OFFSET_DATE_TIME = MOCK_TIME_SOURCE.offsetDateTime();

  static final AddRolesToUserCommand ADD_ANALYST_ROLE_COMMAND = AddRolesToUserCommand
      .builder()
      .username(USERNAME)
      .rolesToAdd(singleton("Analyst"))
      .timeSource(MOCK_TIME_SOURCE)
      .build();

  static final UserDto USER_DTO = UserDto
      .builder()
      .userName(USERNAME)
      .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, emptyList())))
      .origin(SENS_ORIGIN)
      .build();
}
