package com.silenteight.warehouse.report.billing.v1.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportDefinition.PREVIOUS_YEAR;
import static com.silenteight.warehouse.report.billing.v1.domain.DeprecatedReportDefinition.THIS_YEAR;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedListBillingReportsRestController.class)
class DeprecatedListReportsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_REPORTS_URL = "/v1/analysis/production/definitions/BILLING";

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    get(LIST_REPORTS_URL)
        .statusCode(OK.value())
        .body("[0].title", is(THIS_YEAR.getTitle()))
        .body("[1].title", is(PREVIOUS_YEAR.getTitle()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(LIST_REPORTS_URL).statusCode(FORBIDDEN.value());
  }
}
