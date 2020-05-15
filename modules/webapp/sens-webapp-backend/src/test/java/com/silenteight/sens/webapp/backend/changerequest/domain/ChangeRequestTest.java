package com.silenteight.sens.webapp.backend.changerequest.domain;

import com.silenteight.sens.webapp.backend.changerequest.domain.exception.ChangeRequestNotInPendingStateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChangeRequestTest {

  private static final UUID BULK_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MAKER_USERNAME = "maker";
  private static final String MAKER_COMMENT = "This is comment from Maker";
  private static final String APPROVER_USERNAME = "approver";

  @Test
  void changeRequestApproved_stateIsChangedToApproved() {
    // give
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);

    // when
    changeRequest.approve(APPROVER_USERNAME);

    // then
    assertThat(changeRequest.getState()).isEqualTo(APPROVED.toString());
    assertThat(changeRequest.getApproverUsername()).isEqualTo(APPROVER_USERNAME);
  }

  @Test
  void changeRequestInApprovedState_approveThrowsChangeRequestNotInPendingStateException() {
    // give
    ChangeRequest changeRequest = new ChangeRequest(BULK_CHANGE_ID, MAKER_USERNAME, MAKER_COMMENT);
    changeRequest.setState(APPROVED.name());

    // when
    Executable when = () -> changeRequest.approve(APPROVER_USERNAME);

    // then
    assertThrows(ChangeRequestNotInPendingStateException.class, when);
  }
}
