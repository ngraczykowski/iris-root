package com.silenteight.sens.webapp.backend.changerequest.pending;

import com.silenteight.sens.webapp.backend.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
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

@Import({ PendingChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class PendingChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private PendingChangeRequestQuery changeRequestsQuery;

  @Nested
  class ListChangeRequests {

    private final Fixtures fixtures = new Fixtures();

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its200_whenNoPendingChangeRequest() {
      given(changeRequestsQuery.listPending()).willReturn(emptyList());

      get(mappingForList())
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      given(changeRequestsQuery.listPending()).willReturn(
          List.of(fixtures.firstChangeRequest, fixtures.secondChangeRequest));

      get(mappingForList())
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0].id", equalTo(1))
          .body("[0].bulkChangeId", equalTo("05bf9714-b1ee-4778-a733-6151df70fca3"))
          .body("[0].createdBy", equalTo("Business Operator #1"))
          .body("[0].createdAt", notNullValue())
          .body("[0].comment", equalTo("Increase efficiency by 20% on Asia markets"))
          .body("[1].id", equalTo(2))
          .body("[1].bulkChangeId", equalTo("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
          .body("[1].createdBy", equalTo("Business Operator #2"))
          .body("[1].createdAt", notNullValue())
          .body("[1].comment", equalTo("Disable redundant RBs based on analyses from 2020.04.02"));
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(mappingForList()).statusCode(FORBIDDEN.value());
    }

    private String mappingForList() {
      return "/change-requests";
    }

    private class Fixtures {

      ChangeRequestDto firstChangeRequest = ChangeRequestDto.builder()
          .id(1L)
          .bulkChangeId(fromString("05bf9714-b1ee-4778-a733-6151df70fca3"))
          .createdBy("Business Operator #1")
          .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
          .comment("Increase efficiency by 20% on Asia markets")
          .build();

      ChangeRequestDto secondChangeRequest = ChangeRequestDto.builder()
          .id(2L)
          .bulkChangeId(fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"))
          .createdBy("Business Operator #2")
          .createdAt(parse("2020-04-10T09:20:30+01:00", ISO_OFFSET_DATE_TIME))
          .comment("Disable redundant RBs based on analyses from 2020.04.02")
          .build();
    }
  }
}
