package com.silenteight.serp.governance.model.get;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_DTO;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetModelRestController.class,
    GetModelControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetModelRestControllerTest extends BaseRestControllerTest {

  private static final String GET_MODEL_URL = "/v1/models/" + MODEL_ID.toString();

  @MockBean
  private GetModelDetailsQuery getModelDetailsQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenModelFound() {
    given(getModelDetailsQuery.get(MODEL_ID)).willReturn(MODEL_DTO);
    get(GET_MODEL_URL)
        .statusCode(OK.value())
        .body("name", is(MODEL_RESOURCE_NAME))
        .body("policyName", is(POLICY_NAME))
        .body("createdAt", notNullValue());
  }

  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its404_whenModelAlreadyExists() {
    when(getModelDetailsQuery.get(MODEL_ID)).thenThrow(ModelNotFoundException.class);

    get(GET_MODEL_URL).statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(GET_MODEL_URL).statusCode(FORBIDDEN.value());
  }
}
