package com.silenteight.sens.webapp.sso.list;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({ ListSsoMappingsRestController.class })
class ListSsoMappingsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_SSO_MAPPING_URL = "/sso/mappings";

  @MockBean
  ListSsoMappingsQuery listSsoMappingsQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListSsoMappingsInvoked() {
    Mockito.when(listSsoMappingsQuery.listAll()).thenReturn(SSO_MAPPING_DTO_LIST);

    get(LIST_SSO_MAPPING_URL).statusCode(OK.value())
        .body("[0].name", is(SS0_NAME))
        .body("[0].attributes[0].attribute", is("Attribute #1"))
        .body("[0].attributes[0].role", is("Role #1"))
        .body("[0].roles[0]", is(USER_ADMINISTRATOR))
        .body("[0].roles[1]", is(AUDITOR))
        .body("[1].name", is(SS0_NAME_2));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(LIST_SSO_MAPPING_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
