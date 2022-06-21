package com.silenteight.serp.governance.model.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.model.create.dto.CreateModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreateModelRestController.class,
    CreateModelControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class CreateModelRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_MODEL_URL = "/v1/solvingModels";

  @MockBean
  private CreateModelUseCase createModelUseCase;

  @ParameterizedTest
  @MethodSource("com.silenteight.serp.governance.policy.domain.SharedTestFixtures#getPolicyNames")
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its202_whenModelCreated(String policyName) {
    post(CREATE_MODEL_URL, makeCreateModelDto(policyName))
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(createModelUseCase).activate(
        CreateModelCommand
            .builder()
            .id(MODEL_ID)
            .policy(policyName)
            .createdBy(USERNAME)
            .build());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its409_whenModelAlreadyExists() {
    when(createModelUseCase.activate(any())).thenThrow(ModelAlreadyExistsException.class);

    post(CREATE_MODEL_URL, makeCreateModelDto(POLICY))
        .contentType(anything())
        .statusCode(CONFLICT.value());
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRole() {
    post(CREATE_MODEL_URL, makeCreateModelDto(POLICY))
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  @ParameterizedTest
  @MethodSource(
      "com.silenteight.serp.governance.policy.domain.SharedTestFixtures#getIncorrectPolicyNames"
  )
  @WithMockUser(username = USERNAME, authorities = MODEL_TUNER)
  void its400_whenPolicyNameLengthIsWrong(String policyName) {
    post(CREATE_MODEL_URL, makeCreateModelDto(policyName))
        .contentType(anything())
        .statusCode(BAD_REQUEST.value());
  }

  private static CreateModelDto makeCreateModelDto(String policyName) {
    return new CreateModelDto(MODEL_ID, policyName);
  }
}
