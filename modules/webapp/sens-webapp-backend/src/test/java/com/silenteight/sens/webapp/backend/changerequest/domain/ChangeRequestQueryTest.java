package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.backend.changerequest.dto.ChangeRequestDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChangeRequestQueryTest {

  @InjectMocks
  private ChangeRequestQuery underTest;

  @Mock
  private ChangeRequestRepository repository;

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
    List<ChangeRequestDto> pending = underTest.listPending();

    // then
    assertThat(pending).extracting(ChangeRequestDto::getId)
        .containsExactly(id1, id2);
    assertThat(pending).extracting(ChangeRequestDto::getBulkChangeId)
        .containsExactly(bulkChangeId1, bulkChangeId2);
    assertThat(pending).extracting(ChangeRequestDto::getCreatedBy)
        .containsExactly(makerUsername1, makerUsername2);
    assertThat(pending).extracting(ChangeRequestDto::getComment)
        .containsExactly(makerComment1, makerComment2);
  }

  private static ChangeRequest makeChangeRequest(
      long id, UUID bulkChangeId, String makerUsername, String makerComment) {

    ChangeRequest changeRequest =
        new ChangeRequest(bulkChangeId, makerUsername, makerComment, now());
    changeRequest.setId(id);

    return changeRequest;
  }
}
