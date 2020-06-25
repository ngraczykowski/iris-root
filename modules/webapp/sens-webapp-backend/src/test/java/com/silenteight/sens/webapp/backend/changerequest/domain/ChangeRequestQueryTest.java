package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.backend.changerequest.closed.ClosedChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.pending.PendingChangeRequestDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRequestQueryTest {

  private static final Integer MAX_CLOSED = 30;

  private ChangeRequestQuery underTest;

  @Mock
  private ChangeRequestRepository repository;

  @BeforeEach
  void setUp() {
    underTest = new ChangeRequestQuery(repository, new ChangeRequestProperties(MAX_CLOSED));
  }

  @Test
  void pendingChangeRequestsAvailable_listPending() {
    // given
    long id1 = 1L;
    UUID bulkChangeId1 = fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");
    String makerUsername1 = "maker_1";
    String makerComment1 = "Comment 1";
    long id2 = 2L;
    UUID bulkChangeId2 = fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");
    String makerUsername2 = "maker_2";
    String makerComment2 = "Comment 2";
    List<ChangeRequest> changeRequests =
        asList(
            makeChangeRequest(id1, bulkChangeId1, makerUsername1, makerComment1),
            makeChangeRequest(id2, bulkChangeId2, makerUsername2, makerComment2));
    given(repository.findAllByState(PENDING)).willReturn(changeRequests);

    // when
    List<PendingChangeRequestDto> pending = underTest.listPending();

    // then
    assertThat(pending).extracting(PendingChangeRequestDto::getId)
        .containsExactly(id1, id2);
    assertThat(pending).extracting(PendingChangeRequestDto::getBulkChangeId)
        .containsExactly(bulkChangeId1, bulkChangeId2);
    assertThat(pending).extracting(PendingChangeRequestDto::getCreatedBy)
        .containsExactly(makerUsername1, makerUsername2);
    assertThat(pending).extracting(PendingChangeRequestDto::getComment)
        .containsExactly(makerComment1, makerComment2);
  }

  @Test
  void queriesRepositoryForClosedChangeRequests() {
    underTest.listClosed();

    verify(repository).findAllByStateIn(
        eq(Set.of(APPROVED, REJECTED, CANCELLED)),
        eq(PageRequest.of(0, MAX_CLOSED, Direction.DESC, "createdAt")));
  }

  @Test
  void returnsClosedChangeRequests() {
    long id1 = 1L;
    UUID bulkChangeId1 = fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");
    String makerUsername1 = "maker_1";
    String makerComment1 = "Comment 1";
    String approverUsername = "approver 1";
    String approverComment = "approver Comment 1";
    OffsetDateTime approvedAt = now().minusSeconds(2);

    ChangeRequest changeRequest1 =
        makeChangeRequest(id1, bulkChangeId1, makerUsername1, makerComment1);
    changeRequest1.approve(approverUsername, approverComment, approvedAt);

    long id2 = 2L;
    UUID bulkChangeId2 = fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");
    String makerUsername2 = "maker_2";
    String makerComment2 = "Comment 2";
    String rejectorUsername = "approver 2";
    String rejectorComment = "approver Comment 2";
    OffsetDateTime rejectedAt = now().minusSeconds(3);

    ChangeRequest changeRequest2 =
        makeChangeRequest(id2, bulkChangeId2, makerUsername2, makerComment2);
    changeRequest2.reject(rejectorUsername, rejectorComment, rejectedAt);

    when(repository.findAllByStateIn(anySet(), any(PageRequest.class)))
        .thenReturn(asList(changeRequest1, changeRequest2));

    List<ClosedChangeRequestDto> closed = underTest.listClosed();

    assertThat(closed).extracting(ClosedChangeRequestDto::getId)
        .containsExactly(id1, id2);
    assertThat(closed).extracting(ClosedChangeRequestDto::getBulkChangeId)
        .containsExactly(bulkChangeId1, bulkChangeId2);
    assertThat(closed).extracting(ClosedChangeRequestDto::getCreatedBy)
        .containsExactly(makerUsername1, makerUsername2);
    assertThat(closed).extracting(ClosedChangeRequestDto::getComment)
        .containsExactly(makerComment1, makerComment2);
    assertThat(closed).extracting(ClosedChangeRequestDto::getDecidedBy)
        .containsExactly(approverUsername, rejectorUsername);
    assertThat(closed).extracting(ClosedChangeRequestDto::getDeciderComment)
        .containsExactly(approverComment, rejectorComment);
    assertThat(closed).extracting(ClosedChangeRequestDto::getDecidedAt)
        .containsExactly(approvedAt, rejectedAt);
    assertThat(closed).extracting(ClosedChangeRequestDto::getState)
        .containsExactly("APPROVED", "REJECTED");
  }

  private static ChangeRequest makeChangeRequest(
      long id, UUID bulkChangeId, String makerUsername, String makerComment) {

    ChangeRequest changeRequest =
        new ChangeRequest(bulkChangeId, makerUsername, makerComment, now());
    changeRequest.setId(id);

    return changeRequest;
  }
}
