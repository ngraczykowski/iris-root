package com.silenteight.sens.webapp.sso.delete;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SSO_ID_1;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ DeleteSsoMappingRestController.class })
class DeleteSsoMappingRestControllerTest extends BaseRestControllerTest {

  private static final String DELETE_SSO_MAPPING_URL = format("/sso/mappings/%s", SSO_ID_1);

  @MockBean
  DeleteSsoMappingUseCase deleteSsoMappingUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenSsoMappingIsDeleted() {
    delete(DELETE_SSO_MAPPING_URL).statusCode(ACCEPTED.value());

    ArgumentCaptor<DeleteSsoMappingRequest> captor =
        ArgumentCaptor.forClass(DeleteSsoMappingRequest.class);

    verify(deleteSsoMappingUseCase, times(1)).activate((captor.capture()));
    DeleteSsoMappingRequest command = captor.getValue();
    assertThat(command.getSsoMappingId()).isEqualTo(SSO_ID_1);
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    delete(DELETE_SSO_MAPPING_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
