package com.silenteight.warehouse.report.reasoning.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.GENERATING;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    StatusAiReasoningReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class StatusAiReasoningReportRestControllerTest extends BaseRestControllerTest {

  private static final String STATUS_LABEL = "status";
  private static final String REPORT_NAME_LABEL = "reportName";
  private static final String OK_STATUS = "OK";
  private static final String GENERATING_STATUS = "GENERATING";
  private static final String STATUS_SIMULATION_REPORT_URL =
      fromUriString(
          "/v2/analysis/{analysisId}/reports/AI_REASONING/{id}/status")
          .build(of(
              "analysisId", ANALYSIS_ID,
              "id", REPORT_ID))
          .toString();

  private static final String STATUS_PRODUCTION_REPORT_URL =
      fromUriString(
          "/v2/analysis/production/reports/AI_REASONING/{id}/status")
          .build(of("id", REPORT_ID))
          .toString();

  @MockBean
  private AiReasoningReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingSimulationReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(STATUS_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(GENERATING_STATUS))
        .body(REPORT_NAME_LABEL, is(getReportName(ANALYSIS_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedSimulationReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(STATUS_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(OK_STATUS))
        .body(REPORT_NAME_LABEL, is(getReportName(ANALYSIS_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(GENERATING_STATUS))
        .body(REPORT_NAME_LABEL, is(getReportName(PRODUCTION_ANALYSIS_NAME)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(OK_STATUS))
        .body(REPORT_NAME_LABEL, is(getReportName(PRODUCTION_ANALYSIS_NAME)));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingSimulationReport() {
    get(STATUS_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  private String getReportName(String analysis) {
    return format("analysis/%s/reports/AI_REASONING/%d", analysis, REPORT_ID);
  }
}
