package com.silenteight.simulator.management;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static com.silenteight.simulator.management.SimulationRestController.SIMULATIONS_URL;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    SimulationRestController.class,
    GenericExceptionControllerAdvice.class,
    SimulationControllerAdvice.class
})
class SimulationRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private CreateSimulationUseCase createSimulationUseCase;

  @MockBean
  private SimulationQuery simulationQuery;

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

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenSimulationFound() {
    given(simulationQuery.list()).willReturn(of(SIMULATION_DTO));
    get(SIMULATIONS_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(SIMULATION_ID.toString()))
        .body("[0].name", is(NAME))
        .body("[0].state", is(STATE.toString()))
        .body("[0].datasetNames", is(DATASET_NAMES))
        .body("[0].modelName", is(MODEL_NAME))
        .body("[0].progressState", is(PROGRESS_STATE))
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(SimulationFixtures.USERNAME));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForListing() {
    get(SIMULATIONS_URL).statusCode(FORBIDDEN.value());
  }
}
