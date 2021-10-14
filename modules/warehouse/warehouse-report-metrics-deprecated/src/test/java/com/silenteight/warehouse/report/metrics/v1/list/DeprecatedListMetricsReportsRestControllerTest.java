package com.silenteight.warehouse.report.metrics.v1.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.DAY;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.MONTH;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.THREE_MONTHS;
import static com.silenteight.warehouse.report.metrics.v1.domain.DeprecatedMetricsReportDefinition.WEEK;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedListMetricsReportsRestController.class)
class DeprecatedListMetricsReportsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_METRICS_REPORT_URL =
      "/v1/analysis/production/definitions/METRICS";

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    get(LIST_METRICS_REPORT_URL)
        .statusCode(OK.value())
        .body("[0].title", is(DAY.getTitle()))
        .body("[1].title", is(WEEK.getTitle()))
        .body("[2].title", is(MONTH.getTitle()))
        .body("[3].title", is(THREE_MONTHS.getTitle()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(LIST_METRICS_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}