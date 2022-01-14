package com.silenteight.sens.webapp.role.list;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListRolesRestController.class })
class ListRolesRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_ROLES_URL = "/v2/roles";

  @MockBean
  private ListRolesQuery listRolesQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListRolesInvoked() {
    when(listRolesQuery.listAll()).thenReturn(ROLE_DTOS_LIST);

    get(LIST_ROLES_URL).statusCode(OK.value())
        .body("[0].id", is(ROLE_ID_1.toString()))
        .body("[0].name", is(ROLE_NAME_1))
        .body("[0].description", is(ROLE_DESCRIPTION_1))
        .body("[0].createdBy", is(USERNAME_1))
        .body("[0].updatedBy", is(USERNAME_1))
        .body("[1].id", is(ROLE_ID_2.toString()))
        .body("[1].name", is(ROLE_NAME_2))
        .body("[1].description", is(ROLE_DESCRIPTION_2))
        .body("[1].createdBy", is(USERNAME_2))
        .body("[1].updatedBy", is(USERNAME_2));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(LIST_ROLES_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
