/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest;

import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.user.rest.UserRestControllerFixtures.APPROVER_USER;
import static com.silenteight.sens.webapp.user.rest.UserRestControllerFixtures.AUDITOR_USER;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

class UserRestControllerListUsersTest extends UserRestControllerTest {

  @TestWithRole(role = USER_ADMINISTRATOR)
  void its200WithUsersList() {
    given(listUsersUseCase.apply())
        .willReturn(asList(AUDITOR_USER, APPROVER_USER));

    get(getRequestPath())
        .statusCode(OK.value())
        .body("size", is(2))
        .body("[0].userName", equalTo(AUDITOR_USER.getUserName()))
        .body("[0].displayName", equalTo(AUDITOR_USER.getDisplayName()))
        .body("[0].roles", equalTo(AUDITOR_USER.getRoles()))
        .body("[0].countryGroups", equalTo(AUDITOR_USER.getCountryGroups()))
        .body("[0].lastLoginAt", notNullValue())
        .body("[0].createdAt", notNullValue())
        .body("[0].deletedAt", nullValue())
        .body("[0].origin", equalTo(AUDITOR_USER.getOrigin()))
        .body("[1].userName", equalTo(APPROVER_USER.getUserName()))
        .body("[1].displayName", equalTo(APPROVER_USER.getDisplayName()))
        .body("[1].roles", equalTo(APPROVER_USER.getRoles()))
        .body("[1].countryGroups", equalTo(APPROVER_USER.getCountryGroups()))
        .body("[1].lastLoginAt", notNullValue())
        .body("[1].createdAt", notNullValue())
        .body("[1].deletedAt", nullValue())
        .body("[1].origin", equalTo(APPROVER_USER.getOrigin()));
  }

  @Deprecated
  @TestWithRole(role = USER_ADMINISTRATOR)
  void its200WithPageableUsersList_Deprecated() {
    given(listUsersUseCase.apply())
        .willReturn(asList(AUDITOR_USER, APPROVER_USER));

    get(getRequestPath() + "/pageable?page=0&size=9999")
        .statusCode(OK.value())
        .body("content.size", is(2))
        .body("content[0].userName", equalTo(AUDITOR_USER.getUserName()))
        .body("content[0].displayName", equalTo(AUDITOR_USER.getDisplayName()))
        .body("content[0].roles", equalTo(AUDITOR_USER.getRoles()))
        .body("content[0].countryGroups", equalTo(AUDITOR_USER.getCountryGroups()))
        .body("content[0].lastLoginAt", notNullValue())
        .body("content[0].createdAt", notNullValue())
        .body("content[0].deletedAt", nullValue())
        .body("content[0].origin", equalTo(AUDITOR_USER.getOrigin()))
        .body("content[1].userName", equalTo(APPROVER_USER.getUserName()))
        .body("content[1].displayName", equalTo(APPROVER_USER.getDisplayName()))
        .body("content[1].roles", equalTo(APPROVER_USER.getRoles()))
        .body("content[1].countryGroups", equalTo(APPROVER_USER.getCountryGroups()))
        .body("content[1].lastLoginAt", notNullValue())
        .body("content[1].createdAt", notNullValue())
        .body("content[1].deletedAt", nullValue())
        .body("content[1].origin", equalTo(APPROVER_USER.getOrigin()));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(getRequestPath()).statusCode(FORBIDDEN.value());
  }

  private static String getRequestPath() {
    return "/users";
  }
}
