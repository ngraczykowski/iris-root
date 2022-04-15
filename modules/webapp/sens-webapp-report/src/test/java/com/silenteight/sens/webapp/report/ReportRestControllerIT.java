package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.QA;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.sens.webapp.report.ReportTestFixtures.REPORT_NAME;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ContextConfiguration(classes = ReportTestConfiguration.class)
class ReportRestControllerIT extends BaseRestControllerTest {

  @MockBean
  private AuditTracer auditTracer;

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  @SuppressWarnings("squid:S2699")
  void notFoundResponseWhenNoReportNameFound() {
    get("/reports/WRONG_REPORT_NAME").statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void reportDataWhenValidReportNameRequested() {
    given()
        .queryParam("paramA", "paramValueABC")
        .queryParam("paramB", "paramValueEFG")
        .accept("text/csv")
        .when().get(RestConstants.ROOT + "/reports/" + REPORT_NAME)
        .then().contentType("text/csv")
        .log().ifValidationFails()
        .statusCode(OK.value())
        .headers(Map.of("Content-Disposition", "attachment; filename=" + REPORT_NAME))
        .body(
            containsString(
                "Requested query parameters:\nparamA=paramValueABC\nparamB=paramValueEFG"));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = USER_ADMINISTRATOR)
  void badRequestResponseIfMandatoryParameterNotProvided() {
    get("/reports/" + REPORT_NAME).statusCode(BAD_REQUEST.value());
  }

  @TestWithRole(roles = { QA })
  void its403_whenNotPermittedRole() {
    get("/reports/WRONG_REPORT_NAME").statusCode(FORBIDDEN.value());
  }
}
