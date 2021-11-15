package com.silenteight.warehouse.report.metrics.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.*;
import static java.util.Map.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateMetricsReportRestController.class,
    CreateMetricsReportControllerAdvice.class,
})
class CreateMetricsReportRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v2/analysis/{analysisId}/reports/METRICS")
          .build(of("analysisId", ANALYSIS_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/METRICS")
          .queryParam("from", OFFSET_DATE_TIME_FROM)
          .queryParam("to", OFFSET_DATE_TIME_TO)
          .build()
          .toString();

  @MockBean
  private CreateSimulationMetricsReportUseCase simulationMetricsReportUseCase;
  @MockBean
  private CreateProductionMetricsReportUseCase productionMetricsReportUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(simulationMetricsReportUseCase.createReport(ANALYSIS_ID)).willReturn(
        REPORT_INSTANCE_REFERENCE_DTO);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "METRICS/" + REPORT_ID + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(productionMetricsReportUseCase.createReport(any(), any())).willReturn(
        REPORT_INSTANCE_REFERENCE_DTO);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "METRICS/" + REPORT_ID + "/status");
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
