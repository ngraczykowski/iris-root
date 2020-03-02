package com.silenteight.sens.webapp.user.dto;

import org.junit.jupiter.api.Test;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static com.silenteight.sens.webapp.user.dto.UserDtoFixtures.ANALYST_ROLE_USER;
import static com.silenteight.sens.webapp.user.dto.UserDtoFixtures.NO_ROLES_USER;
import static com.silenteight.sens.webapp.user.dto.UserDtoFixtures.TWO_ROLES_USER;
import static org.assertj.core.api.Assertions.*;

class UserDtoTest {

  @Test
  void hasOnlyRoleIsFalse_whenUserHasNoRoles() {
    // when
    boolean result = NO_ROLES_USER.hasOnlyRole(ANALYST);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void hasOnlyRoleIsTrue_whenUserHasAnalystRole() {
    // when
    boolean result = ANALYST_ROLE_USER.hasOnlyRole(ANALYST);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void hasOnlyRoleIsFalse_whenUserHasDifferentRoles() {
    // when
    boolean result = ANALYST_ROLE_USER.hasOnlyRole("other-role");

    // then
    assertThat(result).isFalse();
  }

  @Test
  void hasOnlyRoleIsFalse_whenUserHasTwoRoles() {
    // when
    boolean result = TWO_ROLES_USER.hasOnlyRole(ANALYST);

    // then
    assertThat(result).isFalse();
  }
}
