package com.silenteight.serp.governance.changerequest.list;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.list.dto.ChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.fixture.ChangeRequestFixtures.MODEL_NAME;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ListChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class ListChangeRequestRestControllerTest extends BaseRestControllerTest {

  private static final String PENDING_CHANGE_REQUESTS_URL = "/v1/changeRequests?state=PENDING";

  private final Fixtures fixtures = new Fixtures();

  @MockBean
  private ListChangeRequestsQuery changeRequestsQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200_whenNoPendingChangeRequest() {
    given(changeRequestsQuery.list(Set.of(PENDING))).willReturn(emptyList());

    get(PENDING_CHANGE_REQUESTS_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(changeRequestsQuery.list(Set.of(PENDING))).willReturn(
        List.of(fixtures.firstChangeRequest, fixtures.secondChangeRequest));

    get(PENDING_CHANGE_REQUESTS_URL)
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0].id", equalTo("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .body("[0].createdBy", equalTo("Business Operator #1"))
        .body("[0].createdAt", notNullValue())
        .body("[0].creatorComment", equalTo("Increase efficiency by 20% on Asia markets"))
        .body("[1].modelName", equalTo(MODEL_NAME))
        .body("[1].id", equalTo("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
        .body("[1].createdBy", equalTo("Business Operator #2"))
        .body("[1].createdAt", notNullValue())
        .body("[1].creatorComment",
            equalTo("Disable redundant RBs based on analyses from 2020.04.02"))
        .body("[1].modelName", equalTo(MODEL_NAME));
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    get(PENDING_CHANGE_REQUESTS_URL).statusCode(FORBIDDEN.value());
  }

  private class Fixtures {

    ChangeRequestDto firstChangeRequest = ChangeRequestDto.builder()
        .id(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .createdBy("Business Operator #1")
        .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
        .creatorComment("Increase efficiency by 20% on Asia markets")
        .modelName(MODEL_NAME)
        .state(PENDING.name())
        .build();

    ChangeRequestDto secondChangeRequest = ChangeRequestDto.builder()
        .id(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
        .createdBy("Business Operator #2")
        .createdAt(parse("2020-04-10T09:20:30+01:00", ISO_OFFSET_DATE_TIME))
        .creatorComment("Disable redundant RBs based on analyses from 2020.04.02")
        .modelName(MODEL_NAME)
        .state(PENDING.name())
        .build();
  }
}
