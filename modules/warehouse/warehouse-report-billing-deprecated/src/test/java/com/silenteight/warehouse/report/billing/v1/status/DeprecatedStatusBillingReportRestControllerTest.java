package com.silenteight.warehouse.report.billing.v1.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportState.GENERATING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedStatusBillingReportRestController.class)
class DeprecatedStatusBillingReportRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 5;

  @MockBean
  DeprecatedReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get("/v1/analysis/production/definitions/BILLING/3aa046a1-6c0f-4ac2-bd79-635147db1e01/"
        + "reports/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("GENERATING"))
        .body(
            "reportName",
            is("analysis/production/definitions/BILLING/3aa046a1-6c0f-4ac2-bd79-635147db1e01/"
                + "reports/" + REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get("/v1/analysis/production/definitions/BILLING/3aa046a1-6c0f-4ac2-bd79-635147db1e01"
        + "/reports/" + REPORT_ID + "/status")
        .statusCode(OK.value())
        .body("status", is("OK"))
        .body(
            "reportName",
            is("analysis/production/definitions/BILLING/3aa046a1-6c0f-4ac2-bd79-635147db1e01"
                + "/reports/" + REPORT_ID));
  }
}
