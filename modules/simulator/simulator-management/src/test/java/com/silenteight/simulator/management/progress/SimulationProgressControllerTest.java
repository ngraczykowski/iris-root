package com.silenteight.simulator.management.progress;

import com.silenteight.simulator.common.testing.rest.BaseRestControllerTest;
import com.silenteight.simulator.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.simulator.common.testing.rest.TestRoles.*;
import static java.util.UUID.fromString;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Import({ SimulationProgressController.class, })
class SimulationProgressControllerTest extends BaseRestControllerTest {

  @MockBean
  private SimulationProgressUseCase simulationProgressUseCase;

  private static final String ANALYSIS_PROGRESS_URL =
      "/v1/simulations/0609c84d-4e86-4244-8838-634f50c42951/progress";

  private static final SimulationProgressDto ANALYSIS_STATISTIC_DTO =
      SimulationProgressDto.builder()
          .allAlerts(254)
          .solvedAlerts(200)
          .indexedAlerts(0)
          .build();

  @TestWithRole(roles = { MODEL_TUNER })
  void its200_whenDownloadingAnalysisStatistics() {
    when(simulationProgressUseCase.activate(fromString("0609c84d-4e86-4244-8838-634f50c42951")))
        .thenReturn(ANALYSIS_STATISTIC_DTO);

    get(ANALYSIS_PROGRESS_URL).statusCode(200)
        .body("allAlerts", is(254))
        .body("solvedAlerts", is(200))
        .body("indexedAlerts", is(0));
  }

  @TestWithRole(roles = { APPROVER, AUDITOR, QA, QA_ISSUE_MANAGER, USER_ADMINISTRATOR })
  void its400whenNotPermittedRoleForStatistic() {
    get(ANALYSIS_PROGRESS_URL).statusCode(FORBIDDEN.value());
  }
}
