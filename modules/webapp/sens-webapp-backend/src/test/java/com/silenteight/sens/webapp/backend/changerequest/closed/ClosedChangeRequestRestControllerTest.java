package com.silenteight.sens.webapp.backend.changerequest.closed;

import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.APPROVER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.BUSINESS_OPERATOR;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@Import({ ClosedChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class ClosedChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ClosedChangeRequestQuery closedChangeRequestsQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200_whenNoPendingChangeRequest() {
    given(closedChangeRequestsQuery.listClosed()).willReturn(emptyList());

    get(closedChangeRequestsPath())
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    given(closedChangeRequestsQuery.listClosed()).willReturn(
        List.of(
            ClosedChangeRequestDto.builder()
                .id(1L)
                .bulkChangeId(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
                .createdBy("Business Operator #1")
                .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
                .comment("Increase efficiency by 20% on Asia markets")
                .decidedBy("Approver 1")
                .deciderComment("All good!")
                .decidedAt(parse("2020-04-15T10:16:40+01:00", ISO_OFFSET_DATE_TIME))
                .state("ACCEPTED")
                .build(),
            ClosedChangeRequestDto.builder()
                .id(2L)
                .bulkChangeId(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
                .createdBy("Business Operator #2")
                .createdAt(parse("2020-04-10T09:20:30+01:00", ISO_OFFSET_DATE_TIME))
                .comment("Disable redundant RBs based on analyses from 2020.04.02")
                .decidedBy("Approver 2")
                .deciderComment("Not good!")
                .decidedAt(parse("2020-05-04T08:16:40+01:00", ISO_OFFSET_DATE_TIME))
                .state("REJECTED")
                .build()));

    get(closedChangeRequestsPath())
        .statusCode(OK.value())
        .body("size()", is(2))
        .body("[0].id", equalTo(1))
        .body("[0].bulkChangeId", equalTo("05bf9714-b1ee-4778-a733-6151df70fca3"))
        .body("[0].createdBy", equalTo("Business Operator #1"))
        .body("[0].createdAt", notNullValue())
        .body("[0].comment", equalTo("Increase efficiency by 20% on Asia markets"))
        .body("[0].decidedBy", equalTo("Approver 1"))
        .body("[0].deciderComment", equalTo("All good!"))
        .body("[0].decidedAt", notNullValue())
        .body("[0].state", equalTo("ACCEPTED"))
        .body("[1].id", equalTo(2))
        .body("[1].bulkChangeId", equalTo("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
        .body("[1].createdBy", equalTo("Business Operator #2"))
        .body("[1].createdAt", notNullValue())
        .body("[1].comment", equalTo("Disable redundant RBs based on analyses from 2020.04.02"))
        .body("[1].decidedBy", equalTo("Approver 2"))
        .body("[1].deciderComment", equalTo("Not good!"))
        .body("[1].decidedAt", notNullValue())
        .body("[1].state", equalTo("REJECTED"));
  }

  private String closedChangeRequestsPath() {
    return "/change-requests/closed";
  }
}
