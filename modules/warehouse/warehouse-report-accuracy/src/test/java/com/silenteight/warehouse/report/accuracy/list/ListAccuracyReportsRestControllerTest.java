package com.silenteight.warehouse.report.accuracy.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(ListAccuracyReportsRestController.class)
class ListAccuracyReportsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_ACCURACY_REPORT_URL =
      "/v1/analysis/production/definitions/ACCURACY";

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    get(LIST_ACCURACY_REPORT_URL)
        .statusCode(OK.value())
        .body("[0].title", is(DAY.getTitle()))
        .body("[1].title", is(WEEK.getTitle()))
        .body("[2].title", is(MONTH.getTitle()))
        .body("[3].title", is(FIVE_MONTHS.getTitle()))
        .body("[4].title", is(ONE_YEAR.getTitle()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(LIST_ACCURACY_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}