package com.silenteight.sens.webapp.user.roles.delete;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ DeleteRoleRestController.class })
class DeleteRoleRestControllerTest extends BaseRestControllerTest {

  private static final String DELETE_ROLE_URL = "/v2/roles/629c5176-413d-43ef-8c94-ad0417bd89b9";

  @MockBean
  DeleteRoleUseCase deleteRoleUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenRoleCreated() {
    doNothing().when(deleteRoleUseCase).activate(any());

    delete(DELETE_ROLE_URL).statusCode(ACCEPTED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    delete(DELETE_ROLE_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
