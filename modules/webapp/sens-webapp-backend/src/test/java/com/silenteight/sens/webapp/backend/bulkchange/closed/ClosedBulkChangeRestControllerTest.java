package com.silenteight.sens.webapp.backend.bulkchange.closed;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeRestControllerAdvice;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ClosedBulkChangeRestController.class,
    BulkChangeRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class ClosedBulkChangeRestControllerTest extends BaseRestControllerTest {

  private static final String URL = "/bulk-changes/closed";

  @MockBean
  private ClosedBulkChangeQuery bulkChangeQuery;

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithEmptyList_whenNoPendingBulkChanges() {
    given(bulkChangeQuery.listClosed()).willReturn(emptyList());

    get(URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("size()", is(0));
  }

  @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
  void its200WithCorrectBody_whenFound() {
    UUID id1 = randomUUID();
    UUID id2 = randomUUID();
    given(bulkChangeQuery.listClosed()).willReturn(List.of(
        new BulkChangeDto(id1,
            List.of(new ReasoningBranchIdDto(1L, 12L), new ReasoningBranchIdDto(2L, 13L)),
            "FALSE_POSITIVE", TRUE, now()),
        new BulkChangeDto(
            id2, List.of(new ReasoningBranchIdDto(4L, 15L)), "POTENTIAL_TRUE_POSITIVE", FALSE,
            now()))
    );

    get(URL)
        .contentType(anything())
        .statusCode(OK.value())
        .body("[0].id", is(id1.toString()))
        .body("[0].reasoningBranchIds[0].decisionTreeId", is(1))
        .body("[0].reasoningBranchIds[0].featureVectorId", is(12))
        .body("[0].reasoningBranchIds[1].decisionTreeId", is(2))
        .body("[0].reasoningBranchIds[1].featureVectorId", is(13))
        .body("[0].aiSolution", is("FALSE_POSITIVE"))
        .body("[0].active", is(TRUE))
        .body("[1].id", is(id2.toString()))
        .body("[1].reasoningBranchIds[0].decisionTreeId", is(4))
        .body("[1].reasoningBranchIds[0].featureVectorId", is(15))
        .body("[1].aiSolution", is("POTENTIAL_TRUE_POSITIVE"))
        .body("[1].active", is(FALSE));
  }

  @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
  void its403_whenNotPermittedRole() {
    get(URL).statusCode(FORBIDDEN.value());
  }
}