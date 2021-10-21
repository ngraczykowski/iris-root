package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.rbs.RbsReportTestFixtures.*;
import static java.util.Map.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateRbsReportRestController.class,
    CreateRbsReportControllerAdvice.class,
})
class CreateRbsReportRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v2/analysis/{analysisId}/reports/RB_SCORER")
          .build(of("analysisId", ANALYSIS_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/RB_SCORER")
          .queryParam("from", FROM_QUERY_PARAM)
          .queryParam("to", TO_QUERY_PARAM)
          .build()
          .toString();

  @MockBean
  private CreateSimulationRbsReportUseCase simulationRbsReportUseCase;
  @MockBean
  private CreateProductionRbsReportUseCase productionRbsReportUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(simulationRbsReportUseCase.createReport(ANALYSIS_ID)).willReturn(
        REPORT_INSTANCE_REFERENCE_DTO);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "RB_SCORER/" + REPORT_ID + "/status");
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(productionRbsReportUseCase.createReport(any(), any())).willReturn(
        REPORT_INSTANCE_REFERENCE_DTO);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "RB_SCORER/" + REPORT_ID + "/status");
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
