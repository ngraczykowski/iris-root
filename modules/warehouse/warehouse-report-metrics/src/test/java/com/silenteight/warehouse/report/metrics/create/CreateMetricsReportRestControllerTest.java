package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.*;
import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.DAY;
import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.SIMULATION;
import static java.util.Map.of;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateMetricsReportRestController.class,
    CreateMetricsReportControllerAdvice.class,
    CreateMetricsReportUseCase.class,
})
class CreateMetricsReportRestControllerTest extends BaseRestControllerTest {

  private static final MetricsReportDefinition TYPE = DAY;
  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v1/analysis/{analysisId}/definitions/METRICS/{id}/reports")
          .build(of("analysisId", ANALYSIS_ID, "id", SIMULATION_DEFINITION_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v1/analysis/production/definitions/METRICS/{id}/reports")
          .build(of("id", DAY_DEFINITION_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL_WRONG_DEFINITION_ID =
      fromUriString("/v1/analysis/production/definitions/METRICS/{id}/reports")
          .build(of("id", "f1673499-2c10-4db8-912f-3ed9f09db3e2"))
          .toString();

  @MockBean
  private MetricsReportService service;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(service.createSimulationReportInstance(SIMULATION, ANALYSIS_ID))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(service.createProductionReportInstance(TYPE, PRODUCTION_ANALYSIS_NAME))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its400_whenWrongProductionReportDefinitionId() {
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
