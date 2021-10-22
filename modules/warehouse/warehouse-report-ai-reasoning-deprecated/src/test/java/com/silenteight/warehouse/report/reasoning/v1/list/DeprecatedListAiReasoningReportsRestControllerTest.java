package com.silenteight.warehouse.report.reasoning.v1.list;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportDefinition.MONTH;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import(DeprecatedListAiReasoningReportsRestController.class)
class DeprecatedListAiReasoningReportsRestControllerTest extends BaseRestControllerTest {

  private static final String LIST_AI_REASONING_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING";

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    get(LIST_AI_REASONING_REPORT_URL)
        .statusCode(OK.value())
        .body("[0].title", is(MONTH.getTitle()));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void its403_whenNotPermittedRoleForGetQaAlert() {
    get(LIST_AI_REASONING_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}