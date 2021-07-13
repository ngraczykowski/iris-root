package com.silenteight.warehouse.report.rbs.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.DAY;
import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.MONTH;
import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.THREE_MONTHS;
import static com.silenteight.warehouse.report.rbs.domain.ReportDefinition.WEEK;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(ListReportsRestController.class)
class ListReportsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_REPORTS_URL = "/v1/analysis/production/definitions/RB_SCORER";

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    get(LIST_REPORTS_URL)
        .statusCode(OK.value())
        .body("[0].name", is(DAY.getName()))
        .body("[1].name", is(WEEK.getName()))
        .body("[2].name", is(MONTH.getName()))
        .body("[3].name", is(THREE_MONTHS.getName()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(LIST_REPORTS_URL).statusCode(FORBIDDEN.value());
  }
}
