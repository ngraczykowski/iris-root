package com.silenteight.sens.webapp.user.roles.edit;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.EDIT_ROLE_DTO;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.PERMISSIONS;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.ROLE_ID;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ EditRoleRestController.class })
class EditRoleRestControllerTest extends BaseRestControllerTest {

  private static final String EDIT_ROLE_URL = "/v2/roles";
  private static final String ASSIGN_PERMISSION_TO_ROLE_URL =
      format("/v2/roles/%s/permissions", ROLE_ID);

  @MockBean
  EditRoleUseCase editRoleUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenRoleEditInvoked() {
    doNothing().when(editRoleUseCase).activate(any());

    patch(EDIT_ROLE_URL, EDIT_ROLE_DTO).statusCode(ACCEPTED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleOnEditRole() {
    patch(EDIT_ROLE_URL, EDIT_ROLE_DTO)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its201WhenAssignPermissionToRoleInvoked() {
    doNothing().when(editRoleUseCase).activate(any());

    put(ASSIGN_PERMISSION_TO_ROLE_URL, PERMISSIONS).statusCode(ACCEPTED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleOnAssignPermissionsToRole() {
    put(ASSIGN_PERMISSION_TO_ROLE_URL, PERMISSIONS)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
