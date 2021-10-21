package com.silenteight.warehouse.report.rbs.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.GENERATING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(StatusRbsReportRestController.class)
class ReportStatusRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;

  @MockBean
  RbsReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get("/v2/analysis/production/reports/RB_SCORER/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body("reportName", is("analysis/production/reports/RB_SCORER/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get("/v2/analysis/production/reports/RB_SCORER/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body("reportName", is("analysis/production/reports/RB_SCORER/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingSimReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        GENERATING);

    get("/v2/analysis/123/reports/RB_SCORER/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body(
            "reportName",
            is("analysis/123/reports/RB_SCORER/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedSimReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(
        DONE);

    get("/v2/analysis/123/reports/RB_SCORER/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body("reportName", is("analysis/123/reports/RB_SCORER/" + REPORT_ID));
  }
}
