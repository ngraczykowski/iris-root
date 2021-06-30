package com.silenteight.sens.webapp.scb.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserFixtures {

  private static final OffsetDateTime DATE_TIME = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
  private static final String ROLES_SCOPE = "frontend";

  static final Collection<UserDto> SINGLE_ROLE_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("User Administrator"))))
          .origin(SENS_ORIGIN)
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("analyst"))))
          .origin(SENS_ORIGIN)
          .build());

  static final Collection<UserDto> MULTIPLE_ROLES_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("User Administrator"))))
          .origin(SENS_ORIGIN)
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, asList("analyst", "auditor"))))
          .origin(SENS_ORIGIN)
          .build());
}
