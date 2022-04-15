package com.silenteight.sens.webapp.user.list;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.Map;

import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ListUsersWithRoleUseCaseFixtures {

  static final String ROLES_SCOPE = "dev-frontend";
  static final String COUNTRY_GROUPS_NAME = "adad4d35-2bbc-4fac-b12b-436bd2ee6703";

  static final UserDto USER_LIST_DTO = UserDto.builder()
      .userName("username")
      .displayName("username")
      .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList(COUNTRY_GROUPS_NAME))))
      .origin("external")
      .build();
}
