package com.silenteight.simulator.management.statistics;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.simulator.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static com.silenteight.simulator.management.SimulationFixtures.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    SimulationStatisticsRestController.class,
    GenericExceptionControllerAdvice.class
})
@ContextConfiguration(classes = { SimulationStatisticsTestConfiguration.class})
class SimulationStatisticsRestControllerTest extends BaseRestControllerTest {

  static final String EFFICIENCY_URL = format("/v1//simulations/%s/statistics/efficiency", ID);
  static final String EFFECTIVENESS_URL = format("/v1/simulations/%s/statistics/effectiveness", ID);

  @MockBean
  private StaticSimulationStatisticsService simulationStatisticsService;

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its200_whenSimulationStatisticsEfficiencyFound() {
    given(simulationStatisticsService
        .getEfficiency(ID))
        .willReturn(SIMULATION_STATISTICS_EFFICIENCY);
    get(EFFICIENCY_URL)
        .statusCode(OK.value())
        .body("solvedAlerts", is((int) SOLVED_ALERTS))
        .body("allAlerts", is((int) ALL_ALERTS));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForStatisticsEfficiency() {
    get(EFFICIENCY_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(role = BUSINESS_OPERATOR)
  void its200_whenSimulationStatisticsEffectivenessFound() {
    given(simulationStatisticsService
        .getEffectiveness(ID))
        .willReturn(SIMULATION_STATISTICS_EFFECTIVENESS);
    get(EFFECTIVENESS_URL)
        .statusCode(OK.value())
        .body("aiSolvedAsFalsePositive", is((int) AI_SOLVED_AS_FALSE_POSITIVE))
        .body("analystSolvedAsFalsePositive", is((int) ANALYSTS_SOLVED_AS_FALSE_POSITIVE));
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForStatisticsEffectiveness() {
    get(EFFECTIVENESS_URL).statusCode(FORBIDDEN.value());
  }
}
