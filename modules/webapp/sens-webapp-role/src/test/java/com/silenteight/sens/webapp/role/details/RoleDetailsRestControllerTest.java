package com.silenteight.sens.webapp.role.details;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ RoleDetailsRestController.class })
class RoleDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String GET_ROLE_DETAILS_URL = format("/v2/roles/%s", ROLE_ID_1);

  @MockBean
  RoleDetailsQuery roleDetailsQuery;

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its200_whenInvoked() {
    given(roleDetailsQuery.details(ROLE_ID_1)).willReturn(ROLE_DETAILS);

    get(GET_ROLE_DETAILS_URL).statusCode(OK.value())
        .body("id", is(ROLE_ID_1.toString()))
        .body("name", is(ROLE_NAME_1))
        .body("description", is(ROLE_DESCRIPTION_1))
        .body("createdBy", is(USERNAME_1))
        .body("updatedBy", is(USERNAME_1));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(GET_ROLE_DETAILS_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
