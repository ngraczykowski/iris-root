package com.silenteight.serp.governance.qa.analysis.view;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.domain.DecisionService;
import com.silenteight.serp.governance.qa.domain.exception.AlertAlreadyProcessedException;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertNameException;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.ANALYSIS;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Import({
    ViewAlertAnalysisRestController.class,
    ViewAlertAnalysisConfiguration.class,
    AlertControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class ViewAlertAnalysisRestControllerTest extends BaseRestControllerTest {

  private static final UUID ALERT_ID = fromString("015d8483-b3b1-467f-801a-3530037f220a");
  private static final String ALERT_NAME = format("alerts/%s", ALERT_ID);
  private static final String ALERTS_VIEW_URL = format("/v1/qa/0/alerts/%s:viewing", ALERT_ID);

  @MockBean
  DecisionService decisionService;

  ViewDecisionCommand command = getViewDecisionCommand();

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its404_whenAlertNotFound() {
    doThrow(new WrongAlertNameException(ALERT_NAME)).when(decisionService)
        .view(command.getAlertName(), command.getLevel());

    post(ALERTS_VIEW_URL).statusCode(NOT_FOUND.value());

    verify(decisionService, times(1))
        .view(command.getAlertName(), command.getLevel());
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its400_whenAlertAlreadyProcessed() {
    doThrow(new AlertAlreadyProcessedException(ALERT_NAME)).when(decisionService)
        .view(command.getAlertName(), command.getLevel());

    post(ALERTS_VIEW_URL).statusCode(BAD_REQUEST.value());

    verify(decisionService, times(1))
        .view(command.getAlertName(), command.getLevel());
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenAlertViewed() {
    post(ALERTS_VIEW_URL).statusCode(ACCEPTED.value());

    verify(decisionService, times(1))
        .view(command.getAlertName(), command.getLevel());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    post(ALERTS_VIEW_URL).statusCode(FORBIDDEN.value());
  }

  private ViewDecisionCommand getViewDecisionCommand() {
    return ViewDecisionCommand
        .builder()
        .alertName(ALERT_NAME)
        .level(ANALYSIS)
        .build();
  }
}
