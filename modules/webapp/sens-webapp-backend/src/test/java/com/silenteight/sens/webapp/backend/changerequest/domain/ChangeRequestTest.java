package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotInPendingStateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChangeRequestTest {

  private static final UUID BULK_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";
  private static final String APPROVER_USERNAME = "approver";
  private static final OffsetDateTime CREATED_AT = OffsetDateTime.now();

  @Test
  void changeRequestApproved_stateIsChangedToApproved() {
    // give
    OffsetDateTime approvedAt = parse("2020-05-20T10:15:30+01:00");
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT,
        CREATED_AT);

    // when
    changeRequest.approve(APPROVER_USERNAME, approvedAt);

    // then
    assertThat(changeRequest.getState()).isEqualTo(APPROVED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(changeRequest.getDecidedAt()).isEqualTo(approvedAt);
  }

  @Test
  void changeRequestInApprovedState_approveThrowsChangeRequestNotInPendingStateException() {
    // give
    OffsetDateTime approvedAt = parse("2020-05-20T10:15:30+01:00");
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT,
        CREATED_AT);
    changeRequest.setState(APPROVED);

    // when
    Executable when = () -> changeRequest.approve(APPROVER_USERNAME, approvedAt);

    // then
    assertThrows(ChangeRequestNotInPendingStateException.class, when);
  }

  @Test
  void changeRequestRejected_stateIsChangedToRejected() {
    // give
    OffsetDateTime rejectedAt = parse("2020-05-20T10:15:30+01:00");
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT,
        CREATED_AT);

    // when
    changeRequest.reject(APPROVER_USERNAME, rejectedAt);

    // then
    assertThat(changeRequest.getState()).isEqualTo(REJECTED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
  }

  @Test
  void changeRequestInRejectedState_rejectThrowsChangeRequestNotInPendingStateException() {
    // give
    OffsetDateTime rejectedAt = parse("2020-05-20T10:15:30+01:00");
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT,
        CREATED_AT);
    changeRequest.setState(REJECTED);

    // when
    Executable when = () -> changeRequest.reject(APPROVER_USERNAME, rejectedAt);

    // then
    assertThrows(ChangeRequestNotInPendingStateException.class, when);
  }
}
