package com.silenteight.simulator.dataset.create;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.create.dto.CreateDatasetRequestDto;
import com.silenteight.simulator.dataset.create.exception.EmptyDatasetException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.Stream;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.dataset.create.CreateDatasetRestController.DATASET_URL;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATE_DATASET_REQUEST_DTO;
import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.CREATE_DATASET_REQUEST_WITH_NULL_DESCRIPTION;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreateDatasetRestController.class,
    CreateDatasetControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class CreateDatasetRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateDatasetUseCase createDatasetUseCase;

  private static Stream<CreateDatasetRequestDto> validCreateDatasetRequestDtoProvider() {
    return Stream.of(
        CREATE_DATASET_REQUEST_DTO,
        CREATE_DATASET_REQUEST_WITH_NULL_DESCRIPTION);
  }

  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  @ParameterizedTest
  @MethodSource("validCreateDatasetRequestDtoProvider")
  void its201_whenDatasetCreated(CreateDatasetRequestDto request) {
    doNothing().when(createDatasetUseCase).activate(any());

    post(DATASET_URL, request)
        .statusCode(CREATED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its400_whenDatasetIsEmpty() {
    doThrow(EmptyDatasetException.class)
        .when(createDatasetUseCase).activate(any());

    post(DATASET_URL, CREATE_DATASET_REQUEST_DTO)
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, USER_ADMINISTRATOR, QA, AUDITOR, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post(DATASET_URL, CREATE_DATASET_REQUEST_DTO)
        .contentType(JSON)
        .statusCode(FORBIDDEN.value());
  }
}
