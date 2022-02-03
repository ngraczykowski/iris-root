package com.silenteight.sens.webapp.sso.create;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.CREATE_SSO_MAPPING_DTO;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ CreateSsoMappingRestController.class })
class CreateSsoMappingRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SSO_MAPPING_URL = "/sso/mappings";

  @MockBean
  CreateSsoMappingUseCase createSsoMappingUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its201WhenSsoMappingIsCreated() {
    doNothing().when(createSsoMappingUseCase).activate(any());

    post(CREATE_SSO_MAPPING_URL, CREATE_SSO_MAPPING_DTO).statusCode(CREATED.value());
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(CREATE_SSO_MAPPING_URL, CREATE_SSO_MAPPING_DTO)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
