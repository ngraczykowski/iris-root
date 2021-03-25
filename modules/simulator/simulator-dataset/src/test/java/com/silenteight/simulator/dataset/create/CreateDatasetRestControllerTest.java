package com.silenteight.simulator.dataset.create;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.create.CreateDatasetFixtures.CREATE_DATASET_REQUEST;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreateDatasetRestController.class,
    GenericExceptionControllerAdvice.class
})
class CreateDatasetRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateDatasetUseCase createDatasetUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its201_whenDatasetCreated() {
    doNothing().when(createDatasetUseCase).activate(any(), any());
    post("/v1/dataset", CREATE_DATASET_REQUEST)
        .statusCode(CREATED.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post("/v1/dataset", CREATE_DATASET_REQUEST)
        .contentType(JSON)
        .statusCode(FORBIDDEN.value());
  }
}
