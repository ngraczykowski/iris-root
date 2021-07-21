package com.silenteight.warehouse.report.sm.state;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.sm.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.sm.domain.ReportState.GENERATING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    StatusSimulationMetricsReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class ReportStatusRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;
  private static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  private static final String REPORT_STATUS_URL =
      fromUriString("/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/"
                        + "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e/reports/{id}/status")
          .build(Map.of("analysisId", ANALYSIS_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  private SimulationMetricsReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(REPORT_STATUS_URL)
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body("reportName", is(getReportName()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(REPORT_STATUS_URL)
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body("reportName", is(getReportName()));
  }

  @NotNull
  private String getReportName() {
    return "analysis/" + ANALYSIS_ID + "/definitions/SIMULATION_METRICS/"
        + "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e/reports/" + REPORT_ID;
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingReport() {
    get(REPORT_STATUS_URL).statusCode(FORBIDDEN.value());
  }
}
