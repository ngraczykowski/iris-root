package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.AI_REASONING_TYPE;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.GET_REPORT_DEF_BY_TYPE_AI_REASONING;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.REPORTS_DEFINITION_DTOS;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ProductionReportsController.class,
    GenericExceptionControllerAdvice.class
})
class ProductionReportsControllerTest extends BaseRestControllerTest {

  @MockBean
  ProductionReportingQuery productionReportingQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedGetKibanaReportDefinitionList() {

    when(productionReportingQuery.getReportsDefinitions(AI_REASONING_TYPE))
        .thenReturn(REPORTS_DEFINITION_DTOS);

    get(GET_REPORT_DEF_BY_TYPE_AI_REASONING)
        .statusCode(OK.value())
        .body("reportDefinitionDtoList[0].title", is("AI REASONING"));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetKibanaReportDefinitionList() {
    get(GET_REPORT_DEF_BY_TYPE_AI_REASONING).statusCode(FORBIDDEN.value());
  }
}
