package com.silenteight.warehouse.report.reporting;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ReportsController.class,
    GenericExceptionControllerAdvice.class
})
class ReportsControllerTest extends BaseRestControllerTest {

  @MockBean
  private ReportingService reportingService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedGetReports() {

    given(reportingService.getReportDtoList(ANALYSIS_NAME))
        .willReturn(KIBANA_REPORTS_WRAPPER_TEST);
    get(GET_REPORTS_BY_ANALYSIS_NAME).statusCode(OK.value())
        .body("reports[0].title", is(FILE_NAME))
        .body("reports[0].id", is(REPORT_ID));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetReports() {
    get(GET_REPORTS_BY_ANALYSIS_NAME).statusCode(FORBIDDEN.value());
  }

  @Test
  @SneakyThrows
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedGetTenantName() {

    given(reportingService.getTenantNameWrapperByAnalysis(ANALYSIS_NAME))
        .willReturn(TENANT_NAME_WRAPPER);

    get(GET_TENANT_BY_ANALYSIS_NAME).statusCode(OK.value()).body(
        "tenantName", is(ADMIN_TENANT));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetTenantName() {
    get(GET_TENANT_BY_ANALYSIS_NAME).statusCode(FORBIDDEN.value());
  }
}
