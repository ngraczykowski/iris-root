package com.silenteight.serp.governance.model.get;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_DTO;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_RESOURCE_NAME;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetModelRestController.class,
    GetModelControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetModelRestControllerTest extends BaseRestControllerTest {

  private static final String GET_MODEL_URL = "/v1/solvingModels/" + MODEL_ID.toString();
  private static final String GET_MODEL_BY_POLICY_URL = "/v1/solvingModels?policy=" + POLICY;

  @MockBean
  private GetModelDetailsQuery getModelDetailsQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenModelFound() {
    given(getModelDetailsQuery.get(MODEL_ID)).willReturn(MODEL_DTO);

    get(GET_MODEL_URL)
        .statusCode(OK.value())
        .body("name", is(MODEL_RESOURCE_NAME))
        .body("policy", is(POLICY))
        .body("createdAt", notNullValue());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its200_whenModelByPolicyFound() {
    given(getModelDetailsQuery.getByPolicy(POLICY)).willReturn(List.of(MODEL_DTO));

    get(GET_MODEL_BY_POLICY_URL)
        .statusCode(OK.value())
        .body("[0].name", is(MODEL_RESOURCE_NAME))
        .body("[0].policy", is(POLICY))
        .body("[0].createdAt", notNullValue());
  }

  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its404_whenModelDoesNotExist() {
    given(getModelDetailsQuery.get(MODEL_ID)).willThrow(ModelNotFoundException.class);

    get(GET_MODEL_URL).statusCode(NOT_FOUND.value());
  }

  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its404_whenModelForPolicyDoesNotExist() {
    given(getModelDetailsQuery.getByPolicy(POLICY)).willThrow(ModelNotFoundException.class);

    get(GET_MODEL_BY_POLICY_URL).statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get(GET_MODEL_URL).statusCode(FORBIDDEN.value());
    get(GET_MODEL_BY_POLICY_URL).statusCode(FORBIDDEN.value());
  }
}
