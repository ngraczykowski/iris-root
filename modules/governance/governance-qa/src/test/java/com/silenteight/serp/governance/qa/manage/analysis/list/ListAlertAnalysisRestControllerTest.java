package com.silenteight.serp.governance.qa.manage.analysis.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.manage.analysis.GenericNewAlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.qa.DecisionFixture.STATE_NEW;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static java.util.List.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    AlertControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
    ListAlertAnalysisRestController.class
})
class ListAlertAnalysisRestControllerTest extends BaseRestControllerTest {

  private static final List<DecisionState> ALERT_STATES = List.of(STATE_NEW);
  private static final Integer LIMIT = 2;
  private static final String CREATED_AT = "2021-05-27T18:37:25.654894+02:00";
  private static final String ALERTS_LIST_URL = format("/v1/qa/0/alerts?state=%s&pageSize=%d",
      STATE_NEW, LIMIT);
  private static final String ALERTS_LIST_WITH_PAGE_TOKEN_URL =
      format("/v1/qa/0/alerts?state=%s&pageSize=%d&pageToken=%s", STATE_NEW, LIMIT, CREATED_AT);
  private static final String ALERTS_LIST_WITH_INVALID_PAGE_TOKEN_URL =
      format("/v1/qa/0/alerts?state=%s&pageSize=%d&pageToken=%s", STATE_NEW, LIMIT, "invalid");
  private static final OffsetDateTime MIN_DATE = OffsetDateTime.parse("1970-01-01T00:00:00+00");
  private static final String HEADER_TOTAL_ITEMS = "X-Total-Items";
  private static final String HEADER_NEXT_ITEM = "Next-Page-Token";

  @MockBean
  ListAlertQuery listQuery;

  @TestWithRole(roles = { QA, QA_ISSUE_MANAGER, AUDITOR })
  void its200_whenAlertsListIsEmpty() {
    OffsetDateTime createdAt = OffsetDateTime.parse(CREATED_AT);
    given(listQuery.list(ALERT_STATES, createdAt, LIMIT)).willReturn(of());

    get(ALERTS_LIST_WITH_PAGE_TOKEN_URL)
        .statusCode(OK.value())
        .header(HEADER_TOTAL_ITEMS, is("0"))
        .body("size()", is(0));

    verify(listQuery, times(1)).list(ALERT_STATES, createdAt, LIMIT);
  }

  @TestWithRole(roles = { QA, QA_ISSUE_MANAGER, AUDITOR })
  void its200AndDefaultCreatedAtDateSet_whenCreatedAtNotProvided() {
    given(listQuery.list(ALERT_STATES, MIN_DATE, LIMIT)).willReturn(of());

    get(ALERTS_LIST_URL).statusCode(OK.value()).body("size()", is(0));

    verify(listQuery, times(1)).list(ALERT_STATES, MIN_DATE, LIMIT);
  }

  @TestWithRole(roles = { QA, AUDITOR, QA_ISSUE_MANAGER })
  void its200_andAlertListReturnedWithNextPageToken_whenAlertDetailsFound() {
    //given
    AlertAnalysisDto firstAnalysis = new GenericNewAlertAnalysisDto();
    AlertAnalysisDto secondAnalysis = new GenericNewAlertAnalysisDto();
    AlertAnalysisDto thirdAnalysis = new GenericNewAlertAnalysisDto();
    List<AlertAnalysisDto> analysisDtos = of(firstAnalysis, secondAnalysis, thirdAnalysis);
    given(listQuery.list(of(STATE_NEW), MIN_DATE, LIMIT)).willReturn(analysisDtos);
    given(listQuery.count(any())).willReturn(analysisDtos.size());
    //when
    //then
    get(ALERTS_LIST_URL)
        .contentType(JSON)
        .header(HEADER_TOTAL_ITEMS, is("3"))
        .header(HEADER_NEXT_ITEM, is(thirdAnalysis.getAddedAt().toString()))
        .statusCode(OK.value())
        .body("[0].alertName", is(firstAnalysis.getAlertName()))
        .body("[0].state", is(firstAnalysis.getState().toString()))
        .body("[0].decisionComment", is(firstAnalysis.getDecisionComment()))
        .body("[0].decisionAt", notNullValue())
        .body("[0].addedAt", notNullValue())
        .body("[1].alertName", is(secondAnalysis.getAlertName()))
        .body("[1].state", is(secondAnalysis.getState().toString()))
        .body("[1].decisionComment", is(secondAnalysis.getDecisionComment()))
        .body("[1].decisionAt", notNullValue())
        .body("[1].addedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, USER_ADMINISTRATOR, MODEL_TUNER })
  void its403_whenNotPermittedRole() {
    get(ALERTS_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { QA, AUDITOR })
  void its400_whenInvalidPageTokenProvided() {
    get(ALERTS_LIST_WITH_INVALID_PAGE_TOKEN_URL).statusCode(BAD_REQUEST.value());
  }
}
