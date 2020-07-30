package com.silenteight.sens.webapp.backend.bulkchange.closed;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeIdsForReasoningBranchDto;
import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeRestControllerAdvice;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    ClosedBulkChangeRestController.class,
    BulkChangeRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class ClosedBulkChangeRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ClosedBulkChangeQuery bulkChangeQuery;

  @Nested
  class ListBulkChanges {

    private static final String CLOSED_BULK_CHANGES_URL = "/bulk-changes?statesFamily=closed";

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its200WithEmptyList_whenNoPendingBulkChanges() {
      given(bulkChangeQuery.listClosed()).willReturn(emptyList());

      get(CLOSED_BULK_CHANGES_URL)
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

      get(CLOSED_BULK_CHANGES_URL)
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
      get(CLOSED_BULK_CHANGES_URL).statusCode(FORBIDDEN.value());
    }
  }

  @Nested
  class ListBulkChangesIds {

    private final Fixtures fixtures = new Fixtures();

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      given(bulkChangeQuery.getIdsOfClosed(fixtures.reasoningBranchIds)).willReturn(
          List.of(
              fixtures.bulkChangeIdsForReasoningBranchDto1,
              fixtures.bulkChangeIdsForReasoningBranchDto2));

      get(mappingForIds(List.of(fixtures.reasoningBranchId1, fixtures.reasoningBranchId2)))
          .statusCode(OK.value())
          .body("size()", is(2))
          .body("[0].reasoningBranchId.decisionTreeId", is((int) fixtures.decisionTreeId1))
          .body("[0].reasoningBranchId.featureVectorId", is((int) fixtures.featureVectorId1))
          .body("[0].bulkChangeIds", hasItems(fixtures.bulkChangeId1.toString()))
          .body("[1].reasoningBranchId.decisionTreeId", is((int) fixtures.decisionTreeId2))
          .body("[1].reasoningBranchId.featureVectorId", is((int) fixtures.featureVectorId2))
          .body("[1].bulkChangeIds", hasItems(fixtures.bulkChangeId2.toString()));
    }

    @TestWithRole(roles = { ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(mappingForIds(List.of(fixtures.reasoningBranchId1, fixtures.reasoningBranchId2)))
          .statusCode(FORBIDDEN.value());
    }

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its400_whenBranchIdIncorrect() {
      get(mappingForIds("abc"))
          .statusCode(BAD_REQUEST.value());

      get(mappingForIds("abc-bcd"))
          .statusCode(BAD_REQUEST.value());
    }

    private String mappingForIds(Collection<ReasoningBranchIdDto> reasoningBranchIds) {
      String reasoningBranchIdsParam = reasoningBranchIds
          .stream()
          .map(id -> id.getDecisionTreeId() + "-" + id.getFeatureVectorId())
          .collect(joining(","));

      return mappingForIds(reasoningBranchIdsParam);
    }

    private String mappingForIds(String reasoningBranchIds) {
      return format(
          "/bulk-changes/ids?reasoningBranchId=%s&statesFamily=closed", reasoningBranchIds);
    }

    private class Fixtures {

      long decisionTreeId1 = 1L;
      long decisionTreeId2 = 2L;

      long featureVectorId1 = 10L;
      long featureVectorId2 = 20L;

      ReasoningBranchIdDto reasoningBranchId1 =
          new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1);
      ReasoningBranchIdDto reasoningBranchId2 =
          new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2);

      List<ReasoningBranchIdDto> reasoningBranchIds =
          List.of(reasoningBranchId1, reasoningBranchId2);

      UUID bulkChangeId1 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
      UUID bulkChangeId2 = fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d");

      BulkChangeIdsForReasoningBranchDto bulkChangeIdsForReasoningBranchDto1 =
          BulkChangeIdsForReasoningBranchDto
              .builder()
              .reasoningBranchId(reasoningBranchId1)
              .bulkChangeIds(List.of(bulkChangeId1))
              .build();

      BulkChangeIdsForReasoningBranchDto bulkChangeIdsForReasoningBranchDto2 =
          BulkChangeIdsForReasoningBranchDto
              .builder()
              .reasoningBranchId(reasoningBranchId2)
              .bulkChangeIds(List.of(bulkChangeId2))
              .build();
    }
  }
}