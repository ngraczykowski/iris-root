package com.silenteight.sens.webapp.user.roles.create;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.user.roles.RolesTestFixtures.CREATE_ROLE_DTO;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CreateRoleRestController.class })
class CreateRoleRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_TOLE_URL = "/v2/roles";

  @MockBean
  CreateRoleUseCase createRoleUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its201WhenRoleCreated() {
    doNothing().when(createRoleUseCase).activate(any());

    post(CREATE_TOLE_URL, CREATE_ROLE_DTO).statusCode(CREATED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(CREATE_TOLE_URL, CREATE_ROLE_DTO)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
