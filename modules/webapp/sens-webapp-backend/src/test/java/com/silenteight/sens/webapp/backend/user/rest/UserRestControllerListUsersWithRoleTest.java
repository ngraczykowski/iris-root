package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import static com.silenteight.sens.webapp.backend.user.rest.UserRestControllerFixtures.AUDITOR_USER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

class UserRestControllerListUsersWithRoleTest extends UserRestControllerTest {

  private static final String ROLE_NAME = "role";

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its200WithUsersList() {
    given(listUsersWithRoleUseCase.apply(ROLE_NAME))
        .willReturn(of(AUDITOR_USER));

    get(getRequestPath())
        .statusCode(OK.value())
        .body("size", is(1))
        .body("[0].userName", equalTo(AUDITOR_USER.getUserName()))
        .body("[0].displayName", equalTo(AUDITOR_USER.getDisplayName()))
        .body("[0].roles", equalTo(AUDITOR_USER.getRoles()))
        .body("[0].countryGroups", equalTo(AUDITOR_USER.getCountryGroups()))
        .body("[0].lastLoginAt", notNullValue())
        .body("[0].createdAt", notNullValue())
        .body("[0].deletedAt", nullValue())
        .body("[0].origin", equalTo(AUDITOR_USER.getOrigin()));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(getRequestPath()).statusCode(FORBIDDEN.value());
  }

  private static String getRequestPath() {
    return "/users/role/" + ROLE_NAME;
  }
}
