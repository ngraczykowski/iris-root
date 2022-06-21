package com.silenteight.sens.webapp.sso.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.IdentityProviderNotFoundException;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SS0_NAME;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SSO_ID_1;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SSO_MAPPING_DTO_1;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({ SsoMappingDetailsRestController.class, SsoMappingDetailsRestControllerAdvice.class })
class SsoMappingDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String DETAILS_SSO_MAPPING_URL = format("/sso/mappings/%s", SSO_ID_1);

  @MockBean
  SsoMappingDetailsQuery ssoMappingDetailsQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListRolesInvoked() {
    when(ssoMappingDetailsQuery.details(SSO_ID_1)).thenReturn(SSO_MAPPING_DTO_1);

    get(DETAILS_SSO_MAPPING_URL).statusCode(OK.value())
        .body("name", is(SS0_NAME))
        .body("attributes[0].attribute", is("Attribute #1"))
        .body("attributes[0].role", is("Role #1"))
        .body("roles[0]", is(USER_ADMINISTRATOR))
        .body("roles[1]", is(AUDITOR));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(DETAILS_SSO_MAPPING_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void shouldCatchSsoRoleMapperNotFoundException() {
    doThrow(SsoRoleMapperNotFoundException.class)
        .when(ssoMappingDetailsQuery)
        .details(SSO_ID_1);

    get(DETAILS_SSO_MAPPING_URL)
        .statusCode(NOT_FOUND.value())
        .body(containsString("Sso role mapper not found."));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void shouldCatchIdentityProviderNotFoundException() {
    doThrow(IdentityProviderNotFoundException.class)
        .when(ssoMappingDetailsQuery)
        .details(SSO_ID_1);

    get(DETAILS_SSO_MAPPING_URL)
        .statusCode(NOT_FOUND.value())
        .body(containsString("No Identity Provider found."));
  }
}
