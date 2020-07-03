package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Import({
    BulkChangeRestController.class,
    BulkChangeRestControllerAdvice.class,
    GenericExceptionControllerAdvice.class })
class BulkChangeRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private BulkChangeQuery bulkChangeQuery;

  @MockBean
  private CreateBulkChangeUseCase createBulkChangeUseCase;

  @Nested
  class ListBulkChanges {

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its200WithEmptyList_whenNoPendingBulkChanges() {
      given(bulkChangeQuery.listPending()).willReturn(emptyList());

      get(mappingForList())
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(roles = { APPROVER, BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      UUID id1 = randomUUID();
      UUID id2 = randomUUID();
      given(bulkChangeQuery.listPending()).willReturn(List.of(
          new BulkChangeDto(id1,
              List.of(new ReasoningBranchIdDto(1L, 12L), new ReasoningBranchIdDto(2L, 13L)),
              "FALSE_POSITIVE", TRUE, now()),
          new BulkChangeDto(
              id2, List.of(new ReasoningBranchIdDto(4L, 15L)), "POTENTIAL_TRUE_POSITIVE", FALSE,
              now()))
      );

      get(mappingForList())
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
      get(mappingForList()).statusCode(FORBIDDEN.value());
    }

    private String mappingForList() {
      return "/bulk-changes";
    }
  }

  @Nested
  class CreateBulkChange {

    @TestWithRole(role = BUSINESS_OPERATOR)
    void its202_onCreate() {
      post(mappingForCreate(), bulkChangeDtoWithDefaults(), defaultHeaders())
          .contentType(anything())
          .statusCode(ACCEPTED.value());
    }

    @TestWithRole(role = BUSINESS_OPERATOR)
    void appliesCreateBulkChangeCommandOnUseCase() {
      reset(createBulkChangeUseCase);
      UUID id = randomUUID();
      List<ReasoningBranchIdDto> reasoningBranchIds = List.of(new ReasoningBranchIdDto(1L, 2L));
      String solution = "FALSE_POSITIVE";
      Boolean active = TRUE;
      post(
          mappingForCreate(),
          new BulkChangeDto(id, reasoningBranchIds, solution, active, now()),
          defaultHeaders());

      ArgumentCaptor<CreateBulkChangeCommand> commandCaptor =
          ArgumentCaptor.forClass(CreateBulkChangeCommand.class);

      verify(createBulkChangeUseCase).apply(commandCaptor.capture());

      CreateBulkChangeCommand command = commandCaptor.getValue();
      assertThat(command.getBulkChangeId()).isEqualTo(id);
      assertThat(command.getAiSolution()).isEqualTo(solution);
      assertThat(command.getReasoningBranchIds()).isEqualTo(reasoningBranchIds);
      assertThat(command.getActive()).isEqualTo(active);
    }

    @Test
    @WithMockUser(roles = BUSINESS_OPERATOR)
    void setsCorrelationIdInThreadLocal() {
      UUID correlationId = randomUUID();
      post(
          mappingForCreate(),
          bulkChangeDtoWithDefaults(),
          Map.of(CORRELATION_ID_HEADER, correlationId));

      assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
    }

    @Test
    @WithMockUser(roles = BUSINESS_OPERATOR)
    void its400_IfNoCorrelationIdProvidedInHeader() {
      post(mappingForCreate(), bulkChangeDtoWithDefaults())
          .statusCode(BAD_REQUEST.value())
          .body("key", equalTo("MissingRequestHeader"))
          .body("extras.headerName", equalTo("CorrelationId"));
    }

    @TestWithRole(role = APPROVER)
    void its403_onCreate_whenNotPermittedRole() {
      post(mappingForCreate(), bulkChangeDtoWithDefaults(), defaultHeaders())
          .contentType(anything())
          .statusCode(FORBIDDEN.value());
    }

    private String mappingForCreate() {
      return "/bulk-changes";
    }

    private BulkChangeDto bulkChangeDtoWithDefaults() {
      return new BulkChangeDto(
          randomUUID(), List.of(new ReasoningBranchIdDto(1L, 2L)), null, null, now());
    }

    private Map<String, UUID> defaultHeaders() {
      return Map.of(CORRELATION_ID_HEADER, randomUUID());
    }
  }

  @Nested
  class GetBulkChangeIds {

    private final Fixtures fixtures = new Fixtures();

    @TestWithRole(roles = { BUSINESS_OPERATOR })
    void its200WithCorrectBody_whenFound() {
      given(bulkChangeQuery.getIds(fixtures.reasoningBranchIds)).willReturn(
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

    @TestWithRole(roles = { APPROVER, ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      get(mappingForIds(List.of(fixtures.reasoningBranchId1, fixtures.reasoningBranchId2)))
          .statusCode(FORBIDDEN.value());
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR })
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
      return format("/bulk-changes/ids?reasoningBranchId=%s", reasoningBranchIds);
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
