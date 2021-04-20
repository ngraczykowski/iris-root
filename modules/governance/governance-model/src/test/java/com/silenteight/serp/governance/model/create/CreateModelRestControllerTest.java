package com.silenteight.serp.governance.model.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.model.create.dto.CreateModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.MODEL_ID;
import static com.silenteight.serp.governance.model.fixture.ModelFixtures.POLICY_NAME;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreateModelRestController.class,
    CreateModelControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class CreateModelRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_MODEL_URL = "/v1/models";

  @MockBean
  private CreateModelUseCase createModelUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its202_whenModelCreated() {
    post(CREATE_MODEL_URL, makeCreateModelDto())
        .contentType(anything())
        .statusCode(ACCEPTED.value());

    verify(createModelUseCase).activate(
        CreateModelCommand
            .builder()
            .id(MODEL_ID)
            .policyName(POLICY_NAME)
            .createdBy(USERNAME)
            .build());
  }

  @WithMockUser(username = USERNAME, authorities = POLICY_MANAGER)
  void its409_whenModelAlreadyExists() {
    when(createModelUseCase.activate(any())).thenThrow(ModelAlreadyExistsException.class);

    post(CREATE_MODEL_URL, makeCreateModelDto())
        .contentType(anything())
        .statusCode(CONFLICT.value());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    post(CREATE_MODEL_URL, makeCreateModelDto())
        .contentType(anything())
        .statusCode(FORBIDDEN.value());
  }

  private static CreateModelDto makeCreateModelDto() {
    return new CreateModelDto(MODEL_ID, POLICY_NAME);
  }
}
