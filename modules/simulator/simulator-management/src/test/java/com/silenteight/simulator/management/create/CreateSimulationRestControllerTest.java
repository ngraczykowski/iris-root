package com.silenteight.simulator.management.create;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.dataset.domain.exception.NonActiveDatasetInSet;
import com.silenteight.simulator.management.domain.NonUniqueSimulationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.stream.Stream;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.create.CreateSimulationRestController.SIMULATIONS_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({
    CreateSimulationRestController.class,
    CreateSimulationControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
})
class CreateSimulationRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateSimulationUseCase createSimulationUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  void its201_whenSimulationCreated() {
    doNothing().when(createSimulationUseCase).activate(any());

    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(CREATED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  void its400_whenSimulationUuidExists() {
    doThrow(NonUniqueSimulationException.class)
        .when(createSimulationUseCase).activate(any());

    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  void its400_whenSimulationCreatedWithExpiredDatasets() {
    doThrow(NonActiveDatasetInSet.class)
        .when(createSimulationUseCase).activate(any());

    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(FORBIDDEN.value());
  }

  private static Stream<CreateSimulationRequest> invalidCreateSimulationRequestProvider() {
    return Stream.of(
        CREATE_SIMULATION_REQUEST_WITH_MODEL_NAME_THAT_EXCEEDED_MAX_LENGTH,
        CREATE_SIMULATION_REQUEST_WITH_MODEL_NAME_THAT_NOT_EXCEEDED_MIN_LENGTH);
  }

  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  @ParameterizedTest
  @MethodSource("invalidCreateSimulationRequestProvider")
  void its400_whenCreateSimulationRequestIsInvalid(CreateSimulationRequest request) {
    post(SIMULATIONS_URL, request)
        .statusCode(BAD_REQUEST.value());
  }

  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  @ParameterizedTest
  @MethodSource("getCharsBreakingFieldRegex")
  void its400_whenCreateSimulationRequestHasInvalidSimulationName(String invalidPart) {
    CreateSimulationRequest createSimulationRequest =
        CreateSimulationRequest.builder()
            .id(ID)
            .simulationName(SIMULATION_NAME + invalidPart)
            .description(DESCRIPTION)
            .model(MODEL_NAME)
            .datasets(DATASETS)
            .createdBy(USERNAME)
            .build();
    post(SIMULATIONS_URL, createSimulationRequest)
        .statusCode(BAD_REQUEST.value());
  }

  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, AUDITOR })
  @ParameterizedTest
  @MethodSource("getCharsBreakingFieldRegex")
  void its400_whenCreateSimulationRequestHasInvalidDescription(String invalidPart) {
    CreateSimulationRequest createSimulationRequest =
        CreateSimulationRequest.builder()
            .id(ID)
            .simulationName(SIMULATION_NAME)
            .description(DESCRIPTION + invalidPart)
            .model(MODEL_NAME)
            .datasets(DATASETS)
            .createdBy(USERNAME)
            .build();
    post(SIMULATIONS_URL, createSimulationRequest)
        .statusCode(BAD_REQUEST.value());
  }

  private static Stream<String> getCharsBreakingFieldRegex() {
    return Stream.of(
        "#", "$", "%", ";", "{", ")", "[", ":", ">"
    );
  }
}
