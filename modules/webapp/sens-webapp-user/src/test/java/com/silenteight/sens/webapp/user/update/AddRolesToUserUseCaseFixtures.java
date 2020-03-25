package com.silenteight.sens.webapp.user.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.user.domain.UserOrigin;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.update.AddRolesToUserUseCase.AddRolesToUserCommand;

import java.time.OffsetDateTime;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AddRolesToUserUseCaseFixtures {

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
      .roles(emptyList())
      .origin(UserOrigin.SENS)
      .build();
}
