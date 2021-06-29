package com.silenteight.simulator.dataset.create;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATE_DATASET_REQUEST_DTO;
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
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its201_whenDatasetCreated() {
    doNothing().when(createDatasetUseCase).activate(any());
    post("/v1/datasets", CREATE_DATASET_REQUEST_DTO)
        .statusCode(CREATED.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post("/v1/datasets", CREATE_DATASET_REQUEST_DTO)
        .contentType(JSON)
        .statusCode(FORBIDDEN.value());
  }
}
