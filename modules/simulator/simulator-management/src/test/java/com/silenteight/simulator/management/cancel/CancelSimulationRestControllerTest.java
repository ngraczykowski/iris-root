package com.silenteight.simulator.management.cancel;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.management.common.SimulationControllerAdvice;
import com.silenteight.simulator.management.domain.exception.SimulationNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.CANCEL_SIMULATION_REQUEST;
import static com.silenteight.simulator.management.SimulationFixtures.ID;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Import({
    CancelSimulationRestController.class,
    SimulationControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class CancelSimulationRestControllerTest extends BaseRestControllerTest {

  private static final String CANCEL_SIMULATION_URL = format("/v1/simulations/%s:cancel", ID);

  @MockBean
  private CancelSimulationUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its204_whenSimulationCanceled() {
    doNothing().when(useCase).activate(any());

    post(CANCEL_SIMULATION_URL, CANCEL_SIMULATION_REQUEST)
        .statusCode(NO_CONTENT.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its404_whenSimulationNotFound() {
    doThrow(SimulationNotFoundException.class).when(useCase).activate(any());

    post(CANCEL_SIMULATION_URL, CANCEL_SIMULATION_REQUEST)
        .statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, AUDITOR, USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCanceling() {
    post(CANCEL_SIMULATION_URL, CANCEL_SIMULATION_REQUEST)
        .statusCode(FORBIDDEN.value());
  }
}
