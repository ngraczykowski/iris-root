package com.silenteight.serp.governance.qa.manage.validation.details;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.manage.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertIdException;
import com.silenteight.serp.governance.qa.manage.validation.DummyAlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.AlertFixture.ALERT_ID;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Import({
    GetAlertValidationDetailsRestController.class,
    AlertControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class GetAlertValidationDetailsRestControllerTest extends BaseRestControllerTest {

  private static final String ALERTS_DETAILS_URL = format("/v1/qa/1/alerts/%s", ALERT_ID);

  @MockBean
  AlertDetailsQuery query;

  @TestWithRole(roles = { AUDITOR, QA, QA_ISSUE_MANAGER })
  void its404_whenAlertDetailsNotFound() {
    WrongAlertIdException exception = new WrongAlertIdException(ALERT_ID);
    given(query.details(ALERT_ID)).willThrow(exception);

    get(ALERTS_DETAILS_URL)
        .statusCode(NOT_FOUND.value())
        .body(containsString(exception.getMessage()));
  }

  @TestWithRole(roles = { AUDITOR, QA, QA_ISSUE_MANAGER })
  void its200_andAlertDetailsReturned_whenFound() {
    AlertValidationDetailsDto validationDetailsDto = new DummyAlertValidationDetailsDto();
    given(query.details(ALERT_ID)).willReturn(validationDetailsDto);

    get(ALERTS_DETAILS_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .body("alertName", is(validationDetailsDto.getAlertName()))
        .body("state", is(validationDetailsDto.getState().toString()))
        .body("decisionComment", is(validationDetailsDto.getDecisionComment()))
        .body("decisionAt", notNullValue())
        .body("addedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    get(ALERTS_DETAILS_URL).statusCode(FORBIDDEN.value());
  }
}
