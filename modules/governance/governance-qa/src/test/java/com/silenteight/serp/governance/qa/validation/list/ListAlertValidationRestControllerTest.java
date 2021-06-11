package com.silenteight.serp.governance.qa.validation.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.serp.governance.qa.common.AlertControllerAdvice;
import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.validation.DummyAlertValidationDto;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    AlertControllerAdvice.class,
    GenericExceptionControllerAdvice.class,
    ListAlertValidationRestController.class,
})
class ListAlertValidationRestControllerTest extends BaseRestControllerTest {

  private static final List<DecisionState> ALERT_STATES = of(STATE_NEW);
  private static final Integer LIMIT = 10;
  private static final String CREATED_AT = "2021-05-27T18:37:25.654894+02:00";
  private static final String ALERTS_LIST_URL =
      format("/v1/qa/1/alerts?state=%s&pageSize=%d", STATE_NEW, LIMIT);
  private static final String ALERTS_LIST_WITH_PAGE_TOKEN_URL =
      format("/v1/qa/1/alerts?state=%s&pageSize=%d&pageToken=%s", STATE_NEW, LIMIT, CREATED_AT);
  private static final String ALERTS_LIST_WITH_INVALID_PAGE_TOKEN_URL =
      format("/v1/qa/1/alerts?state=%s&pageSize=%d&pageToken=%s", STATE_NEW, LIMIT, "invalid");
  private static final OffsetDateTime MIN_DATE = OffsetDateTime.parse("1970-01-01T00:00:00+00");
  private static final String HEADER_TOTAL_ITEMS = "X-Total-Items";
  private static final String HEADER_NEXT_ITEM = "Next-Page-Token";

  @MockBean
  ListAlertValidationQuery alertValidationQuery;

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_whenAlertsListIsEmpty() {
    OffsetDateTime createdAfter = OffsetDateTime.parse(CREATED_AT);
    given(alertValidationQuery.list(ALERT_STATES, createdAfter, LIMIT)).willReturn(of());

    get(ALERTS_LIST_WITH_PAGE_TOKEN_URL)
        .statusCode(OK.value())
        .header(HEADER_TOTAL_ITEMS, is("0"))
        .header(HEADER_NEXT_ITEM, is("null"))
        .body("size()", is(0));
    verify(alertValidationQuery, times(1)).list(ALERT_STATES,
        createdAfter, LIMIT);
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200AndDefaultCreatedAtDateSet_whenPageTokenNotProvided() {
    given(alertValidationQuery.list(ALERT_STATES, MIN_DATE, LIMIT)).willReturn(of());

    get(ALERTS_LIST_URL).statusCode(OK.value()).body("size()", is(0));

    verify(alertValidationQuery, times(1))
        .list(ALERT_STATES, MIN_DATE, LIMIT);
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its200_andAlertDetailsReturnedWithNextToken_whenAlertsFound() {
    //given
    AlertValidationDto alertValidationDto = new DummyAlertValidationDto();
    given(alertValidationQuery.list(ALERT_STATES, MIN_DATE, LIMIT))
        .willReturn(of(alertValidationDto));
    given(alertValidationQuery.count(ALERT_STATES)).willReturn(1);
    //when
    //then
    get(ALERTS_LIST_URL)
        .contentType(JSON)
        .statusCode(OK.value())
        .header(HEADER_TOTAL_ITEMS, is("1"))
        .header(HEADER_NEXT_ITEM, is(alertValidationDto.getAddedAt().toString()))
        .body("[0].alertName", is(alertValidationDto.getAlertName()))
        .body("[0].state", is(alertValidationDto.getState().toString()))
        .body("[0].decisionComment", is(alertValidationDto.getDecisionComment()))
        .body("[0].decisionAt", notNullValue())
        .body("[0].decisionBy", is(alertValidationDto.getDecisionBy()))
        .body("[0].addedAt", notNullValue());
  }

  @TestWithRole(roles = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    get(ALERTS_LIST_URL).statusCode(FORBIDDEN.value());
  }

  @TestWithRole(roles = { BUSINESS_OPERATOR })
  void its400_whenInvalidPageTokenProvided() {
    get(ALERTS_LIST_WITH_INVALID_PAGE_TOKEN_URL).statusCode(BAD_REQUEST.value());
  }
}
