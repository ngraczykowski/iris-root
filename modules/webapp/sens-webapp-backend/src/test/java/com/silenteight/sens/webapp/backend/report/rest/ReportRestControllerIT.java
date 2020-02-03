package com.silenteight.sens.webapp.backend.report.rest;

import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.commons.collections.MapBuilder.from;
import static com.silenteight.sens.webapp.backend.report.rest.ReportTestFixtures.REPORT_NAME;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ContextConfiguration(classes = ReportTestConfiguration.class)
class ReportRestControllerIT extends BaseRestControllerTest {

  @Test
  @SuppressWarnings("squid:S2699")
  void notFoundResponseWhenNoReportNameFound() {
    get("/report/WRONG_REPORT_NAME").statusCode(NOT_FOUND.value());
  }

  @Test
  void reportDataWhenValidReportNameRequested() {
    given()
        .accept("text/csv")
        .when().get(RestConstants.ROOT + "/report/" + REPORT_NAME)
        .then().contentType("text/csv")
        .log().ifValidationFails()
        .statusCode(OK.value())
        .headers(from("Content-Disposition", "attachment; filename=" + REPORT_NAME))
        .content(containsString("first_report_line\nsecond_report_line"));
  }
}
