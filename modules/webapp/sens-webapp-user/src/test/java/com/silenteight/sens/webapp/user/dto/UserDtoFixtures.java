package com.silenteight.sens.webapp.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.silenteight.sens.webapp.user.domain.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UserDtoFixtures {

  static final UserDto NO_ROLES_USER = UserDto
      .builder()
      .userName("jsmith")
      .roles(emptyList())
      .origin(SENS_ORIGIN)
      .build();

  static final UserDto ANALYST_ROLE_USER = UserDto
      .builder()
      .userName("rdoe")
      .roles(singletonList(ANALYST))
      .origin(SENS_ORIGIN)
      .build();

  static final UserDto TWO_ROLES_USER = UserDto
      .builder()
      .userName("jkowalski")
      .roles(asList(ANALYST, "other-role"))
      .origin(SENS_ORIGIN)
      .build();
}
