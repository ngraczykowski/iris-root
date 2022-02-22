package com.silenteight.sens.webapp.role.remove;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.role.RoleTestFixtures.ROLE_ID_1;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({ RemoveRoleRestController.class })
class RemoveRoleRestControllerTest extends BaseRestControllerTest {

  private static final String REMOVE_ROLE_URL = "/v2/roles/" + ROLE_ID_1;

  @MockBean
  private RemoveRoleUseCase removeRoleUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its204WhenRoleRemoved() {
    doNothing().when(removeRoleUseCase).activate(any());

    delete(REMOVE_ROLE_URL).statusCode(NO_CONTENT.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    delete(REMOVE_ROLE_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
