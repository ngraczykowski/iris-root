package com.silenteight.simulator.management.create;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.management.domain.NonUniqueSimulationException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.create.CreateSimulationRestController.SIMULATIONS_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@Import({
    CreateSimulationRestController.class,
    CreateSimulationControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
})
class CreateSimulationRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateSimulationUseCase createSimulationUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its201_whenSimulationCreated() {
    doNothing().when(createSimulationUseCase).activate(any());

    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(CREATED.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its400_whenSimulationUuidExists() {
    doThrow(NonUniqueSimulationException.class)
        .when(createSimulationUseCase).activate(any());

    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForCreating() {
    post(SIMULATIONS_URL, CREATE_SIMULATION_REQUEST)
        .statusCode(FORBIDDEN.value());
  }
}
