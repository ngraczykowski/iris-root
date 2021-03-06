package com.silenteight.serp.governance.qa.manage.analysis.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.manage.analysis.DummyAlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.manage.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_ID;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_NAME;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetAnalysisAlertDetailsRestController.class,
    AlertControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetAnalysisAlertDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String ALERTS_DETAILS_URL = format("/v1/qa/0/alerts/%s", ALERT_ID);

  @MockBean
  AlertDetailsQuery detailsQuery;

  @TestWithRole(roles = { AUDITOR, QA })
  void its404_whenAlertDetailsNotFound() {
    given(detailsQuery.details(ALERT_NAME))
        .willThrow(new WrongAlertNameException(ALERT_NAME));

    get(ALERTS_DETAILS_URL).statusCode(NOT_FOUND.value());
  }

  @TestWithRole(roles = { AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_andAlertDetailsReturned_whenAlertDetailsFound() {
    AlertAnalysisDetailsDto alertAnalysisDetailsDto = new DummyAlertAnalysisDetailsDto();
    given(detailsQuery.details(ALERT_NAME)).willReturn(alertAnalysisDetailsDto);

    get(ALERTS_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(alertAnalysisDetailsDto.getAlertName()))
        .body("state", is(alertAnalysisDetailsDto.getState().toString()))
        .body("decisionComment", is(alertAnalysisDetailsDto.getDecisionComment()))
        .body("decisionAt", notNullValue())
        .body("addedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    get(ALERTS_DETAILS_URL).statusCode(FORBIDDEN.value());
  }
}
