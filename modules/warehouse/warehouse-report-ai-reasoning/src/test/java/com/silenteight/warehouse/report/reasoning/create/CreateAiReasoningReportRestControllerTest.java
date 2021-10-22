package com.silenteight.warehouse.report.reasoning.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.*;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateAiReasoningReportRestController.class,
    CreateProductionAiReasoningReportUseCase.class
})
class CreateAiReasoningReportRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v2/analysis/{analysisId}/reports/AI_REASONING")
          .build(of("analysisId", ANALYSIS_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/AI_REASONING")
          .queryParam("from", FROM_QUERY_PARAM)
          .queryParam("to", TO_QUERY_PARAM)
          .build()
          .toString();

  @MockBean
  private CreateProductionAiReasoningReportUseCase productionUseCase;
  @MockBean
  private CreateSimulationAiReasoningReportUseCase simulationUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(simulationUseCase.createReport(any())).willReturn(REPORT_INSTANCE);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", getReportLocation(REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(productionUseCase.createReport(LOCAL_DATE_FROM, LOCAL_DATE_TO))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", getReportLocation(REPORT_ID));
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

  private static String getReportLocation(long id) {
    return format("AI_REASONING/%d/status", id);
  }
}
