package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.indexer.simulation.analysis.AnalysisDoesNotExistException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA_ISSUE_MANAGER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.indexer.simulation.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.ANALYSIS_ID_PARAM;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.DEFINITIONS_COLLECTION_URL;
import static com.silenteight.warehouse.report.simulation.SimulationTestConstants.REPORT_DEFINITION_DTO;
import static com.silenteight.warehouse.report.simulation.SimulationTestConstants.REPORT_DEFINITION_ID;
import static com.silenteight.warehouse.report.simulation.SimulationTestConstants.REPORT_NAME;
import static java.util.Map.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    SimulationReportsRestController.class,
    SimulationReportsControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class SimulationReportsRestControllerTest extends BaseRestControllerTest {

  private static final String TEST_LIST_REPORT_DEFINITIONS_URL =
      fromUriString(DEFINITIONS_COLLECTION_URL)
          .build(of(ANALYSIS_ID_PARAM, ANALYSIS_ID))
          .toString();

  @MockBean
  private SimulationReportsDefinitionsUseCase simulationReportsDefinitionsUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    given(simulationReportsDefinitionsUseCase.activate(ANALYSIS_ID))
        .willReturn(List.of(REPORT_DEFINITION_DTO));

    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(OK.value())
        .body("[0].id", is(REPORT_DEFINITION_ID))
        .body("[0].title", is(REPORT_NAME));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its404_whenAnalysisNotFound() {
    given(simulationReportsDefinitionsUseCase.activate(ANALYSIS_ID))
        .willThrow(AnalysisDoesNotExistException.class);

    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGetReportList() {
    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(FORBIDDEN.value());
  }
}
