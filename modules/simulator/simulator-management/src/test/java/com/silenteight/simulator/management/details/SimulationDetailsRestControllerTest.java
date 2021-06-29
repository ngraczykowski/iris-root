package com.silenteight.simulator.management.details;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.management.SimulationFixtures;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    SimulationDetailsRestController.class,
    SimulationDetailsRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
})
class SimulationDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String SIMULATION_DETAILS_URL = "/v1/simulations/" + ID;

  @MockBean
  private SimulationDetailsQuery simulationQuery;

  @TestWithRole(roles = { MODEL_TUNER, APPROVER })
  void its200_whenSimulationFound() {
    given(simulationQuery.get(ID)).willReturn(DETAILS_DTO);
    get(SIMULATION_DETAILS_URL)
        .statusCode(OK.value())
        .body("id", is(ID.toString()))
        .body("name", is(NAME))
        .body("simulationName", is(SIMULATION_NAME))
        .body("state", is(STATE.toString()))
        .body("datasets", hasItems(DATASET))
        .body("model", is(MODEL))
        .body("progressState", is(PROGRESS_STATE))
        .body("createdAt", notNullValue())
        .body("createdBy", is(SimulationFixtures.USERNAME));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(SIMULATION_DETAILS_URL).statusCode(FORBIDDEN.value());
  }
}
