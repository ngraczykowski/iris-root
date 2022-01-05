package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.QA_ISSUE_MANAGER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.production.ProductionReportRestController.PRODUCTION_REPORT_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ProductionReportsService.class,
    ProductionReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class ProductionReportRestControllerTest extends BaseRestControllerTest {

  public static final String TYPE = "RB_SCORER";
  public static final String TITLE = "Rb Scorer";
  public static final String NAME = "/v2/rbs";
  @MockBean
  ProductionReportsService productionReportsService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    given(productionReportsService.getListOfReports())
        .willReturn(List.of(ReportTypeDto.builder().type(TYPE)
            .title(TITLE)
            .name(NAME)
            .build()));

    get(PRODUCTION_REPORT_URL).status(OK)
        .body("[0].name", is(NAME))
        .body("[0].type", is(TYPE))
        .body("[0].title", is(TITLE));

  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportListWithNoReports() {
    given(productionReportsService.getListOfReports())
        .willReturn(Lists.emptyList());
    get(PRODUCTION_REPORT_URL).status(OK);

  }

  @Test
  @WithMockUser(username = USERNAME,
      authorities = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGetReportList() {
    get(PRODUCTION_REPORT_URL).status(FORBIDDEN);
  }
}