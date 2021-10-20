package com.silenteight.warehouse.report.billing.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.billing.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.billing.domain.ReportState.GENERATING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(StatusBillingReportRestController.class)
class StatusBillingReportRestControllerTest extends BaseRestControllerTest {

  private static final String GET_STATUS_URL =
      "/v2/analysis/production/reports/BILLING/" + REPORT_ID + "/status";

  @MockBean
  ReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(GET_STATUS_URL)
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body(
            "reportName",
            is("analysis/production/reports/BILLING/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(GET_STATUS_URL)
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body(
            "reportName",
            is("analysis/production/reports/BILLING/" + REPORT_ID));
  }
}
