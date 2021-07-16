package com.silenteight.warehouse.report.rbs.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.rbs.domain.ReportState;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
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
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(ReportState.GENERATING);

    get("/v1/analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
            REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body("reportName",
              is("analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(ReportState.DONE);

    get("/v1/analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" +
             REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body("reportName",
              is("analysis/production/definitions/RB_SCORER/rb-scorer-1-day/reports/" + REPORT_ID));
  }
}
