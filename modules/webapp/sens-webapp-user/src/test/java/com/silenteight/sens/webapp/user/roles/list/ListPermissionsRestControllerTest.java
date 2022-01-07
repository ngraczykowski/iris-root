package com.silenteight.sens.webapp.user.roles.list;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListPermissionsRestController.class })
class ListPermissionsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_PERMISSIONS_URL = "/permissions";
  private static final String LIST_PERMISSIONS_ASSIGNED_ROLE_URL =
      String.format("/v2/roles/%s/permissions", ROLE_ID);

  @MockBean
  ListPermissionQuery listPermissionQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListAllPermissionsInvoked() {
    when(listPermissionQuery.listAll()).thenReturn(PERMISSIONS_DTOS_LIST);

    get(LIST_PERMISSIONS_URL).statusCode(OK.value())
        .body("[0].id", is(PERMISSION_ID_1.toString()))
        .body("[0].name", is(PERMISSION_NAME_1))
        .body("[0].description", is(PERMISSION_DESCRIPTION_1))
        .body("[1].name", is(PERMISSION_NAME_2))
        .body("[1].description", is(PERMISSION_DESCRIPTION_2));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleOnListingAllPermissions() {
    get(LIST_PERMISSIONS_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListPermissionAssignedToRoleInvoked() {
    when(listPermissionQuery.listAll()).thenReturn(PERMISSIONS_DTOS_LIST);

    get(LIST_PERMISSIONS_URL).statusCode(OK.value())
        .body("[0].id", is(PERMISSION_ID_1.toString()))
        .body("[0].group", is(PERMISSION_GOVERNANCE_GROUP))
        .body("[0].name", is(PERMISSION_NAME_1))
        .body("[0].description", is(PERMISSION_DESCRIPTION_1))
        .body("[1].id", is(PERMISSION_ID_2.toString()))
        .body("[1].group", is(PERMISSION_WAREHOUSE_GROUP))
        .body("[1].name", is(PERMISSION_NAME_2))
        .body("[1].description", is(PERMISSION_DESCRIPTION_2));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleOnListingPermissionAssignedToRole() {
    get(LIST_PERMISSIONS_ASSIGNED_ROLE_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
