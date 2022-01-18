package com.silenteight.sens.webapp.sso.identityproviders.list;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static com.silenteight.sens.webapp.sso.identityproviders.IdentityProvidersTestFixtures.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ListIdentityProvidersRestController.class
})
class ListIdentityProvidersRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_IDENTITY_PROVIDERS_URL = "/identity-providers";

  @MockBean
  ListIdentityProvidersQuery listIdentityProvidersQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its200WhenListRolesInvoked() {
    when(listIdentityProvidersQuery.listAll()).thenReturn(IDENTITY_PROVIDER_LIST);

    get(LIST_IDENTITY_PROVIDERS_URL).statusCode(OK.value())
        .body("[0].alias", is(IDP_ALIAS_1))
        .body("[0].displayName", is(IDP_DISPLAY_NAME_1))
        .body("[0].internalId", is(IDP_INTERNAL_ID_1))
        .body("[0].enabled", is(true))
        .body("[1].alias", is(IDP_ALIAS_2))
        .body("[1].displayName", is(IDP_DISPLAY_NAME_2))
        .body("[1].internalId", is(IDP_INTERNAL_ID_2))
        .body("[1].enabled", is(false));
  }

  @Test
  @TestWithRole(roles = { APPROVER, AUDITOR, MODEL_TUNER, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    get(LIST_IDENTITY_PROVIDERS_URL)
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }
}
