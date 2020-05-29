package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestUseCase;
import com.silenteight.sens.webapp.backend.changerequest.create.CreateChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.create.CreateChangeRequestUseCase;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestQuery;
import com.silenteight.sens.webapp.backend.changerequest.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.dto.CreateChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestUseCase;
import com.silenteight.sens.webapp.backend.config.exception.GenericExceptionControllerAdvice;
import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.common.testing.rest.testwithrole.TestWithRole;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.*;
import static java.time.OffsetDateTime.now;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Collections.emptyList;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Import({ ChangeRequestRestController.class, GenericExceptionControllerAdvice.class })
class ChangeRequestRestControllerTest extends BaseRestControllerTest {

  @MockBean
  private ChangeRequestQuery changeRequestsQuery;

  @MockBean
  private CreateChangeRequestUseCase createChangeRequestUseCase;

  @MockBean
  private ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @MockBean
  private RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @Nested
  class ListChangeRequests {

    private final Fixtures fixtures = new Fixtures();

    @TestWithRole(role = APPROVER)
    void its200_whenNoPendingChangeRequest() {
      given(changeRequestsQuery.listPending()).willReturn(emptyList());

      get(mappingForList())
          .contentType(anything())
          .statusCode(OK.value())
          .body("size()", is(0));
    }

    @TestWithRole(role = APPROVER)
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

  @Nested
  class CreateChangeRequest {

    private static final String USERNAME = "usernameABC";

    @Test
    @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
    void its200_whenBusinessOperatorCallsEndpoint() {
      post(mappingForChangeRequests(), changeRequestWithDefaults(), defaultHeaders())
          .statusCode(OK.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
    void callsCreateUseCase() {
      UUID bulkChangeId = randomUUID();
      String comment = "comment ABC";
      OffsetDateTime createdAt = now();

      post(
          mappingForChangeRequests(),
          new CreateChangeRequestDto(bulkChangeId, createdAt, comment),
          defaultHeaders());

      ArgumentCaptor<CreateChangeRequestCommand> commandCaptor =
          ArgumentCaptor.forClass(CreateChangeRequestCommand.class);
      verify(createChangeRequestUseCase).apply(commandCaptor.capture());

      CreateChangeRequestCommand command = commandCaptor.getValue();
      assertThat(command.getBulkChangeId()).isEqualTo(bulkChangeId);
      assertThat(command.getMakerComment()).isEqualTo(comment);
      assertThat(command.getMakerUsername()).isEqualTo(USERNAME);
      assertThat(command.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
    void setsCorrelationIdInThreadLocal() {
      UUID correlationId = randomUUID();
      post(
          mappingForChangeRequests(),
          changeRequestWithDefaults(),
          Map.of(CORRELATION_ID_HEADER, correlationId));

      assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
    }

    @TestWithRole(roles = { APPROVER, ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      post(mappingForChangeRequests(), changeRequestWithDefaults(), defaultHeaders())
          .statusCode(FORBIDDEN.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = BUSINESS_OPERATOR)
    void its400_ifNoCorrelationIdProvidedInHeader() {
      post(
          mappingForChangeRequests(),
          changeRequestWithDefaults())
          .statusCode(BAD_REQUEST.value())
          .body("key", equalTo("Missing request header"))
          .body("extras.headerName", equalTo("CorrelationId"));
    }

    private CreateChangeRequestDto changeRequestWithDefaults() {
      return new CreateChangeRequestDto(randomUUID(), now(), "Comment ABC");
    }

    private String mappingForChangeRequests() {
      return "/change-requests";
    }
  }

  @Nested
  class ApproveChangeRequest {

    private static final String USERNAME = "username";

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void its200_whenApproverCallsEndpoint() {
      long changeRequestId = 2L;

      patch(mappingForApproval(changeRequestId), defaultHeaders()).statusCode(OK.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void callsApproveUseCase() {
      long changeRequestId = 2L;

      patch(mappingForApproval(changeRequestId), defaultHeaders());

      ArgumentCaptor<ApproveChangeRequestCommand> commandCaptor =
          ArgumentCaptor.forClass(ApproveChangeRequestCommand.class);
      verify(approveChangeRequestUseCase).apply(commandCaptor.capture());

      ApproveChangeRequestCommand command = commandCaptor.getValue();
      assertThat(command.getChangeRequestId()).isEqualTo(changeRequestId);
      assertThat(command.getApproverUsername()).isEqualTo(USERNAME);
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      long changeRequestId = 2L;

      patch(mappingForApproval(changeRequestId), defaultHeaders()).statusCode(FORBIDDEN.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void setsCorrelationIdInThreadLocal() {
      long changeRequestId = 2L;
      UUID correlationId = randomUUID();

      patch(mappingForApproval(changeRequestId), Map.of(CORRELATION_ID_HEADER, correlationId));

      assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void its400_ifNoCorrelationIdProvidedInHeader() {
      long changeRequestId = 2L;

      patch(mappingForApproval(changeRequestId))
          .statusCode(BAD_REQUEST.value())
          .body("key", equalTo("Missing request header"))
          .body("extras.headerName", equalTo("CorrelationId"));
    }

    private String mappingForApproval(long id) {
      return "/change-request/" + id + "/approve";
    }
  }

  @Nested
  class RejectChangeRequest {

    private static final String USERNAME = "username";

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void its200_whenApproverCallsEndpoint() {
      long changeRequestId = 2L;

      patch(mappingForRejection(changeRequestId), defaultHeaders()).statusCode(OK.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void callsApproveUseCase() {
      long changeRequestId = 2L;

      patch(mappingForRejection(changeRequestId), defaultHeaders());

      ArgumentCaptor<RejectChangeRequestCommand> commandCaptor =
          ArgumentCaptor.forClass(RejectChangeRequestCommand.class);
      verify(rejectChangeRequestUseCase).apply(commandCaptor.capture());

      RejectChangeRequestCommand command = commandCaptor.getValue();
      assertThat(command.getChangeRequestId()).isEqualTo(changeRequestId);
      assertThat(command.getRejectorUsername()).isEqualTo(USERNAME);
    }

    @TestWithRole(roles = { BUSINESS_OPERATOR, ADMIN, ANALYST, AUDITOR })
    void its403_whenNotPermittedRole() {
      long changeRequestId = 2L;

      patch(mappingForRejection(changeRequestId), defaultHeaders()).statusCode(FORBIDDEN.value());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void setsCorrelationIdInThreadLocal() {
      long changeRequestId = 2L;
      UUID correlationId = randomUUID();

      patch(mappingForRejection(changeRequestId), Map.of(CORRELATION_ID_HEADER, correlationId));

      assertThat(RequestCorrelation.id()).isEqualTo(correlationId);
    }

    @Test
    @WithMockUser(username = USERNAME, roles = APPROVER)
    void its400_ifNoCorrelationIdProvidedInHeader() {
      long changeRequestId = 2L;

      patch(mappingForRejection(changeRequestId))
          .statusCode(BAD_REQUEST.value())
          .body("key", equalTo("Missing request header"))
          .body("extras.headerName", equalTo("CorrelationId"));
    }

    private String mappingForRejection(long id) {
      return "/change-request/" + id + "/reject";
    }
  }

  private static Map<String, UUID> defaultHeaders() {
    return Map.of(CORRELATION_ID_HEADER, randomUUID());
  }
}
