package com.silenteight.serp.governance.changerequest.closed;

import com.silenteight.sens.governance.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.governance.common.testing.rest.testwithrole.TestWithRole;
import com.silenteight.serp.governance.changerequest.closed.dto.ClosedChangeRequestDto;
import com.silenteight.serp.governance.common.web.exception.GenericExceptionControllerAdvice;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.*;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ClosedChangeRequestRestController.class,
    GenericExceptionControllerAdvice.class
})
class ClosedChangeRequestRestControllerTest extends BaseRestControllerTest {

  private static final String CLOSED_CHANGE_REQUESTS_URL = "/v1/changeRequests?state=CLOSED";

  private final Fixtures fixtures = new Fixtures();

  @MockBean
  private ClosedChangeRequestQuery closedChangeRequestQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200_whenNoClosedChangeRequest() {
    given(closedChangeRequestQuery.listClosed()).willReturn(emptyList());

    get(CLOSED_CHANGE_REQUESTS_URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(closedChangeRequestQuery.listClosed()).willReturn(
        of(fixtures.firstChangeRequest, fixtures.secondChangeRequest));

    get(CLOSED_CHANGE_REQUESTS_URL)
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0].id", equalTo("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .body("[0].createdBy", equalTo("Business Operator #1"))
        .body("[0].createdAt", notNullValue())
        .body("[0].creatorComment", equalTo("Increase efficiency by 20% on Asia markets"))
        .body("[0].decidedBy", equalTo("Approver 1"))
        .body("[0].deciderComment", equalTo("All good!"))
        .body("[0].decidedAt", notNullValue())
        .body("[0].state", equalTo("APPROVED"))
        .body("[1].id", equalTo("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
        .body("[1].createdBy", equalTo("Business Operator #2"))
        .body("[1].createdAt", notNullValue())
        .body("[1].creatorComment", equalTo("Disable redundant RBs based on analyses"))
        .body("[1].decidedBy", equalTo("Approver 2"))
        .body("[1].deciderComment", equalTo("Not good!"))
        .body("[1].decidedAt", notNullValue())
        .body("[1].state", equalTo("REJECTED"));
  }

  @TestWithRole(roles = { ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRole() {
    get(CLOSED_CHANGE_REQUESTS_URL).statusCode(FORBIDDEN.value());
  }

  private class Fixtures {

    ClosedChangeRequestDto firstChangeRequest = ClosedChangeRequestDto.builder()
        .id(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .createdBy("Business Operator #1")
        .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
        .creatorComment("Increase efficiency by 20% on Asia markets")
        .decidedBy("Approver 1")
        .deciderComment("All good!")
        .decidedAt(parse("2020-04-15T10:16:40+01:00", ISO_OFFSET_DATE_TIME))
        .state("APPROVED")
        .build();

    ClosedChangeRequestDto secondChangeRequest = ClosedChangeRequestDto.builder()
        .id(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
        .createdBy("Business Operator #2")
        .createdAt(parse("2020-04-10T09:20:30+01:00", ISO_OFFSET_DATE_TIME))
        .creatorComment("Disable redundant RBs based on analyses")
        .decidedBy("Approver 2")
        .deciderComment("Not good!")
        .decidedAt(parse("2020-05-04T08:16:40+01:00", ISO_OFFSET_DATE_TIME))
        .state("REJECTED")
        .build();
  }
}
