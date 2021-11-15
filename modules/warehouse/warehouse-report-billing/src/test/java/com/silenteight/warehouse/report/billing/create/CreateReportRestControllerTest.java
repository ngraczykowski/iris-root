package com.silenteight.warehouse.report.billing.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.OFFSET_DATE_TIME_FROM;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.OFFSET_DATE_TIME_TO;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.billing.BillingReportTestFixtures.REPORT_INSTANCE_REFERENCE_DTO;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateBillingReportRestController.class,
    CreateBillingReportConfiguration.class,
    GenericExceptionControllerAdvice.class,
    CreateBillingReportControllerAdvice.class,
})
class CreateReportRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/BILLING")
          .queryParam("from", OFFSET_DATE_TIME_TO)
          .queryParam("to", OFFSET_DATE_TIME_FROM)
          .build()
          .toString();

  @MockBean
  CreateBillingReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenRequestingReportGeneration() {
    when(useCase.createReport(any(), any())).thenReturn(REPORT_INSTANCE_REFERENCE_DTO);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("location", is("BILLING/" + REPORT_ID + "/status"));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingProductionReports() {
    post(CREATE_PRODUCTION_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
