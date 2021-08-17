package com.silenteight.warehouse.report.statistics.simulation;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.statistics.StatisticsTestFixtures.STATISTICS_DTO;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    SimulationStatisticsRestController.class,
    SimulationStatisticsConfiguration.class
})
class SimulationStatisticsRestControllerTest extends BaseRestControllerTest {

  private static final String ANALYSIS_NAME = "analysis";
  private static final String GET_SIMULATION_STATISTICS_URL =
      "/v1/analysis/" + ANALYSIS_NAME + "/statistics";
  private static final String JSON_CONTENT_TYPE = "application/json";

  @MockBean
  SimulationStatisticsQuery query;

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { MODEL_TUNER, APPROVER, AUDITOR })
  void its200_whenDownloadingSimulationStatistics() {
    when(query.getStatistics(ANALYSIS_NAME)).thenReturn(STATISTICS_DTO);

    get(
        GET_SIMULATION_STATISTICS_URL)
        .statusCode(OK.value())
        .contentType(JSON_CONTENT_TYPE)
        .body("effectiveness.aiSolvedAsFalsePositive", is(5))
        .body("effectiveness.analystSolvedAsFalsePositive", is(10))
        .body("efficiency.solvedAlerts", is(20))
        .body("efficiency.allAlerts", is(25));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingSimulationStatistics() {
    get(GET_SIMULATION_STATISTICS_URL).statusCode(FORBIDDEN.value());
  }
}