package com.silenteight.sens.webapp.report.rest;

import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ADMIN;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.ANALYST;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.report.rest.ReportTestFixtures.REPORT_NAME;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
        .queryParam("paramA", "paramValueABC")
        .queryParam("paramB", "paramValueEFG")
        .accept("text/csv")
        .when().get(RestConstants.ROOT + "/report/" + REPORT_NAME)
        .then().contentType("text/csv")
        .log().ifValidationFails()
        .statusCode(OK.value())
        .headers(Map.of("Content-Disposition", "attachment; filename=" + REPORT_NAME))
        .body(
            containsString(
                "Requested query parameters:\nparamA=paramValueABC\nparamB=paramValueEFG"));
  }

  @TestWithRole(role = AUDITOR)
  void badRequestResponseIfMandatoryParameterNotProvided() {
    get("/report/" + REPORT_NAME).statusCode(BAD_REQUEST.value());
  }

  @TestWithRole(roles = { ADMIN, ANALYST, BUSINESS_OPERATOR })
  void its403_whenNotPermittedRole() {
    get("/report/WRONG_REPORT_NAME").statusCode(FORBIDDEN.value());
  }
}
