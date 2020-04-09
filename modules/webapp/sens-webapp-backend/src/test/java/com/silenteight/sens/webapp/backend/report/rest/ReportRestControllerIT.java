package com.silenteight.sens.webapp.backend.report.rest;

import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.commons.collections.MapBuilder.from;
import static com.silenteight.sens.webapp.backend.report.rest.ReportTestFixtures.REPORT_NAME;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.BUSINESS_OPERATOR;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ContextConfiguration(classes = ReportTestConfiguration.class)
class ReportRestControllerIT extends BaseRestControllerTest {

  @TestWithRole(role = AUDITOR)
  @SuppressWarnings("squid:S2699")
  void notFoundResponseWhenNoReportNameFound() {
    get("/report/WRONG_REPORT_NAME").statusCode(NOT_FOUND.value());
  }

  @TestWithRole(role = AUDITOR)
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

  @TestWithRole(roles = { ADMIN, ANALYST, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get("/report/WRONG_REPORT_NAME").statusCode(FORBIDDEN.value());
  }
}
