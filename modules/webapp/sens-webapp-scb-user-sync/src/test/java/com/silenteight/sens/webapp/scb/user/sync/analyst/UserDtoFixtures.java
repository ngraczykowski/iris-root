package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import java.util.Map;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.domain.GnsOrigin.GNS_ORIGIN;
import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static java.time.OffsetDateTime.parse;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UserDtoFixtures {

  private static final String ROLES_SCOPE = "frontend";

  static final UserDto SENS_USER =
      UserDto
          .builder()
          .userName("jsmith")
          .displayName("John Smith")
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("Analyst"))))
          .origin(SENS_ORIGIN)
          .build();

  static final UserDto GNS_USER_WITHOUT_ANALYST_ROLE =
      UserDto
          .builder()
          .userName("4783589366")
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("Maker"))))
          .origin(GNS_ORIGIN)
          .build();

  static final UserDto GNS_USER_WITHOUT_DISPLAY_NAME =
      UserDto
          .builder()
          .userName("1853900357")
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("Analyst"))))
          .origin(GNS_ORIGIN)
          .build();

  static final UserDto GNS_USER =
      UserDto
          .builder()
          .userName("1122334455")
          .displayName("00001111")
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("Analyst"))))
          .origin(GNS_ORIGIN)
          .build();

  static final UserDto DELETED_GNS_USER =
      UserDto
          .builder()
          .userName("2233445566")
          .displayName("11112222")
          .roles(new ScopeUserRoles(Map.of(ROLES_SCOPE, singletonList("Analyst"))))
          .origin(GNS_ORIGIN)
          .lockedAt(parse("2011-12-03T10:15:30+01:00"))
          .build();
}
