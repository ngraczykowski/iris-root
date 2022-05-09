package com.silenteight.sep.usermanagement.api.dto;

import org.junit.jupiter.api.Test;

import static com.silenteight.sep.usermanagement.api.dto.UserDtoFixtures.*;
import static org.assertj.core.api.Assertions.*;

class UserDtoTest {

  @Test
  void hasOnlyRoleIsFalse_whenUserHasNoRoles() {
    // when
    boolean result = NO_ROLES_USER.hasOnlyRole(ROLE_SCOPE, ANALYST_ROLE);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void hasOnlyRoleIsTrue_whenUserHasAnalystRole() {
    // when
    boolean result = ANALYST_ROLE_USER.hasOnlyRole(ROLE_SCOPE, ANALYST_ROLE);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void hasOnlyRoleIsFalse_whenUserHasDifferentRoles() {
    // when
    boolean result = ANALYST_ROLE_USER.hasOnlyRole(ROLE_SCOPE, "other-role");

    // then
    assertThat(result).isFalse();
  }

  @Test
  void hasOnlyRoleIsFalse_whenUserHasTwoRoles() {
    // when
    boolean result = TWO_ROLES_USER.hasOnlyRole(ROLE_SCOPE, ANALYST_ROLE);

    // then
    assertThat(result).isFalse();
  }
}
