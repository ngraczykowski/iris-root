package com.silenteight.sep.usermanagement.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.List;

import static java.util.Collections.emptyList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UserDtoFixtures {

  static final String ORIGIN = "SENS";
  static final String ROLE_SCOPE = "frontend";
  static final String ANALYST_ROLE = "ANALYST";

  static final UserDto NO_ROLES_USER = UserDto
      .builder()
      .userName("jsmith")
      .roles(new TestUserRoles(ROLE_SCOPE, emptyList()))
      .origin(ORIGIN)
      .build();

  static final UserDto ANALYST_ROLE_USER = UserDto
      .builder()
      .userName("rdoe")
      .roles(new TestUserRoles(ROLE_SCOPE, List.of(ANALYST_ROLE)))
      .origin(ORIGIN)
      .build();

  static final UserDto TWO_ROLES_USER = UserDto
      .builder()
      .userName("jkowalski")
      .roles(new TestUserRoles(ROLE_SCOPE, List.of(ANALYST_ROLE, "other-role")))
      .origin(ORIGIN)
      .build();
}
