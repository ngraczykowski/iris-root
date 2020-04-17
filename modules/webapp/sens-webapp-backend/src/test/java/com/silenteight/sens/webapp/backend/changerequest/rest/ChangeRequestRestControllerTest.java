package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.backend.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchAiSolutionDto.FALSE_POSITIVE;
import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchAiSolutionDto.NO_CHANGE;
import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchStatusDto.ACTIVE;
import static com.silenteight.sens.webapp.backend.changerequest.rest.dto.BranchStatusDto.DISABLED;
import static com.silenteight.sens.webapp.backend.rest.TestRoles.*;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Import({ ChangeRequestRestController.class })
class ChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ChangeRequestQuery changeRequestsQuery;

  @Nested
  class ListChangeRequests {

    private final Fixtures fixtures = new Fixtures();

    @TestWithRole(role = APPROVER)
    void its200_whenNoPendingChangeRequest() {
      given(changeRequestsQuery.pending()).willReturn(emptyList());

      get(mappingForList())
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(role = APPROVER)
    void its200WithCorrectBody_whenFound() {
      given(changeRequestsQuery.pending()).willReturn(
          List.of(fixtures.firstChangeRequest, fixtures.secondChangeRequest));

      get(mappingForList())
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0].id", equalTo(1))
          .body("[0].createdBy", equalTo("Business Operator #1"))
          .body("[0].createdAt", notNullValue())
          .body("[0].affectedBranchesCount", equalTo(35))
          .body("[0].branchAiSolution", equalTo("FALSE_POSITIVE"))
          .body("[0].branchStatus", equalTo("ACTIVE"))
          .body("[0].comment", equalTo("Increase efficiency by 20% on Asia markets"))
          .body("[1].id", equalTo(2))
          .body("[1].createdBy", equalTo("Business Operator #2"))
          .body("[1].createdAt", notNullValue())
          .body("[1].affectedBranchesCount", nullValue())
          .body("[1].branchAiSolution", equalTo("NO_CHANGE"))
          .body("[1].branchStatus", equalTo("DISABLED"))
          .body("[1].comment", equalTo("Disable redundant RBs based on analyses from 2020.04.02"));
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(mappingForList()).statusCode(FORBIDDEN.value());
    }

    private String mappingForList() {
      return "/change-requests";
    }

    private class Fixtures {

      ChangeRequestDto firstChangeRequest = ChangeRequestDto.builder()
          .id(1L)
          .createdBy("Business Operator #1")
          .createdAt(parse("2020-04-15T10:15:30+01:00", ISO_OFFSET_DATE_TIME))
          .affectedBranchesCount(35)
          .branchAiSolution(FALSE_POSITIVE)
          .branchStatus(ACTIVE)
          .comment("Increase efficiency by 20% on Asia markets")
          .build();

      ChangeRequestDto secondChangeRequest = ChangeRequestDto.builder()
          .id(2L)
          .createdBy("Business Operator #2")
          .createdAt(parse("2020-04-10T09:20:30+01:00", ISO_OFFSET_DATE_TIME))
          .affectedBranchesCount(null)
          .branchAiSolution(NO_CHANGE)
          .branchStatus(DISABLED)
          .comment("Disable redundant RBs based on analyses from 2020.04.02")
          .build();
    }
  }
}