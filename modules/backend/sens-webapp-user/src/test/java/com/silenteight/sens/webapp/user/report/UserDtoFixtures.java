package com.silenteight.sens.webapp.user.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.Map;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtoFixtures {

  static final String USERNAME = "jkowalski";
  static final String DISPLAY_NAME = "Jan Kowalski";
  static final String ORIGIN = "SENS";
  static final String ROLES_SCOPE = "frontend";
  static final String ROLE_1 = ANALYST;
  static final String ROLE_2 = "other-role";
  static final String CREATED_AT = "2020-01-01T10:00:12+01:00";
  static final String LAST_LOGIN_AT = "2020-06-03T12:21:55+01:00";
  static final String LOCKED_AT = "2020-07-12T12:21:01+01:00";

  static final UserDto ACTIVE_USER = UserDto
      .builder()
      .userName(USERNAME)
      .displayName(DISPLAY_NAME)
      .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList(ROLE_1))))
      .origin(ORIGIN)
      .lastLoginAt(parse(LAST_LOGIN_AT))
      .createdAt(parse(CREATED_AT))
      .lockedAt(parse(LOCKED_AT))
      .build();

  static final UserDto MULTIPLE_ROLES_USER = UserDto
      .builder()
      .origin(ORIGIN)
      .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, asList(ROLE_1, ROLE_2))))
      .build();
}
