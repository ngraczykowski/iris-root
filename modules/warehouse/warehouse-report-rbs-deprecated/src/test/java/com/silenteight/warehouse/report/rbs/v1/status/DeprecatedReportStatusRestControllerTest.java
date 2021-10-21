package com.silenteight.warehouse.report.rbs.v1.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.rbs.v1.domain.DeprecatedReportState;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedStatusRbsReportRestController.class)
class DeprecatedReportStatusRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;
  @MockBean
  DeprecatedRbsReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        DeprecatedReportState.GENERATING);

    get("/v1/analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
        REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body(
            "reportName",
            is("analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        DeprecatedReportState.DONE);

    get("/v1/analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
        REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body(
            "reportName",
            is("analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }


  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingSimReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        DeprecatedReportState.GENERATING);

    get("/v1/analysis/123/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
        REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body(
            "reportName",
            is("analysis/123/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedSimReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        DeprecatedReportState.DONE);

    get("/v1/analysis/123/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
        REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body(
            "reportName",
            is("analysis/123/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }
}
