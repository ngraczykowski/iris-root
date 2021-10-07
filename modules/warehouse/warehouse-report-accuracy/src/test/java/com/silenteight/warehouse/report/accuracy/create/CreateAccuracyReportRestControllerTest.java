package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.accuracy.domain.exception.ReportTypeNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.simulation.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.DAY_DEFINITION_ID;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.REPORT_INSTANCE;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.SIMULATION_DEFINITION_ID;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.TIMESTAMP;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateAccuracyReportRestController.class,
    CreateAccuracyReportControllerAdvice.class,
    CreateAccuracyReportUseCase.class
})
class CreateAccuracyReportRestControllerTest extends BaseRestControllerTest {

  private static final String WRONG_ID = "f1673499-2c10-4db8-912f-3ed9f09db3e2";
  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v1/analysis/{analysisId}/definitions/ACCURACY/{id}/reports")
          .build(Map.of("analysisId", ANALYSIS_ID, "id", SIMULATION_DEFINITION_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v1/analysis/production/definitions/ACCURACY/{id}/reports")
          .build(Map.of("id", DAY_DEFINITION_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL_WRONG_DEFINITION_ID =
      fromUriString("/v1/analysis/production/definitions/ACCURACY/{id}/reports")
          .build(Map.of("id", WRONG_ID))
          .toString();

  @MockBean
  private CreateAccuracyReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(useCase.createSimulationReport(any()))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(useCase.createProductionReport(any()))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its400_whenWrongProductionReportDefinitionId() {
    when(useCase.createProductionReport(any())).thenThrow(
        new ReportTypeNotFoundException(WRONG_ID));

    post(CREATE_PRODUCTION_REPORT_URL_WRONG_DEFINITION_ID).statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingSimulationReports() {
    post(CREATE_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingProductionReports() {
    post(CREATE_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
