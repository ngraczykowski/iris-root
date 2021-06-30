package com.silenteight.simulator.management.list;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.simulator.management.SimulationFixtures;
import com.silenteight.simulator.management.domain.InvalidModelNameException;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ListSimulationRestController.class,
    ListSimulationControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
})
class ListSimulationRestControllerTest extends BaseRestControllerTest {

  private static final String SIMULATIONS_URL = "/v1/simulations";
  private static final String SIMULATIONS_BY_MODEL_NAME_URL =
      format("/v1/simulations?model=%s", MODEL);

  @MockBean
  private ListSimulationsQuery simulationQuery;

  @TestWithRole(roles = { MODEL_TUNER, AUDITOR, APPROVER, QA, QA_ISSUE_MANAGER })
  void its200_whenSimulationFound() {
    given(simulationQuery.list()).willReturn(of(SIMULATION_DTO));
    get(SIMULATIONS_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(ID.toString()))
        .body("[0].name", is(NAME))
        .body("[0].simulationName", is(SIMULATION_NAME))
        .body("[0].state", is(STATE.toString()))
        .body("[0].datasets", hasItems(DATASET))
        .body("[0].model", is(MODEL))
        .body("[0].progressState", is(PROGRESS_STATE))
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(SimulationFixtures.USERNAME));
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRoleForListing() {
    get(SIMULATIONS_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { MODEL_TUNER, AUDITOR, APPROVER, QA, QA_ISSUE_MANAGER })
  void its200_whenSimulationByModelNameFound() {
    given(simulationQuery.findByModel(MODEL)).willReturn(of(SIMULATION_DTO));
    get(SIMULATIONS_BY_MODEL_NAME_URL)
        .statusCode(OK.value())
        .body("size()", is(1))
        .body("[0].id", is(ID.toString()))
        .body("[0].name", is(NAME))
        .body("[0].simulationName", is(SIMULATION_NAME))
        .body("[0].state", is(STATE.toString()))
        .body("[0].datasets", hasItems(DATASET))
        .body("[0].model", is(MODEL))
        .body("[0].progressState", is(PROGRESS_STATE))
        .body("[0].createdAt", notNullValue())
        .body("[0].createdBy", is(SimulationFixtures.USERNAME));
  }

  @TestWithRole(roles = { MODEL_TUNER, AUDITOR, APPROVER, QA, QA_ISSUE_MANAGER })
  void its404_whenSimulationByModelNameThrowsInvalidModelNameException() {
    given(simulationQuery.findByModel(MODEL)).willThrow(InvalidModelNameException.class);
    get(SIMULATIONS_BY_MODEL_NAME_URL)
        .statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { USER_ADMINISTRATOR })
  void its403_whenNotPermittedRoleForGettingByModelName() {
    get(SIMULATIONS_BY_MODEL_NAME_URL).statusCode(FORBIDDEN.value());
  }
}
