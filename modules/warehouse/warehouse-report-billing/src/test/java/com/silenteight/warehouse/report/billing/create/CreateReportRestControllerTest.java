package com.silenteight.warehouse.report.billing.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@Import({
    CreateBillingReportRestController.class,
    CreateBillingReportConfiguration.class,
    GenericExceptionControllerAdvice.class,
    CreateBillingReportControllerAdvice.class,
})
class CreateReportRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 7;
  @MockBean
  BillingReportService reportService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenRequestingReportGeneration() {
    when(reportService.createReportInstance(ReportDefinition.THIS_YEAR))
        .thenReturn(new ReportInstanceReferenceDto(REPORT_ID));

    post("/v1/analysis/production/definitions/BILLING/"
             + "3aa046a1-6c0f-4ac2-bd79-635147db1e01/reports")
        .statusCode(SEE_OTHER.value())
        .header("location", is("reports/" + REPORT_ID + "/status"));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its406_whenReportTypeUnknown() {
    post("/v1/analysis/production/definitions/BILLING/UNKNOWN_TYPE/reports")
        .statusCode(NOT_ACCEPTABLE.value());
  }
}
