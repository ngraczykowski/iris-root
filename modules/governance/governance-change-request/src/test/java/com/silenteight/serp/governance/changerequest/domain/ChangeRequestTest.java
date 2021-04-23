package com.silenteight.serp.governance.changerequest.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.REJECTED;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

class ChangeRequestTest {

  private static final UUID CHANGE_CHANGE_ID = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String MODEL_NAME = "solving-models/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  private static final String BUSINESS_OPERATOR_USERNAME = "business_operator";
  private static final String BUSINESS_OPERATOR_COMMENT = "This is comment from Business Operator";
  private static final String APPROVER_USERNAME = "approver";
  private static final String APPROVER_COMMENT = "approver comment 1234";

  @Test
  void changeRequestApproved_stateIsChangedToApproved() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        BUSINESS_OPERATOR_USERNAME,
        BUSINESS_OPERATOR_COMMENT);

    // when
    changeRequest.approve(APPROVER_USERNAME, APPROVER_COMMENT);

    // then
    assertThat(changeRequest.getState()).isEqualTo(APPROVED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }

  @Test
  void changeRequestRejected_stateIsChangedToRejected() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        BUSINESS_OPERATOR_USERNAME,
        BUSINESS_OPERATOR_COMMENT);

    // when
    changeRequest.reject(APPROVER_USERNAME, APPROVER_COMMENT);

    // then
    assertThat(changeRequest.getState()).isEqualTo(REJECTED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }

  @Test
  void changeRequestRejected_stateIsChangedToCancelled() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        BUSINESS_OPERATOR_USERNAME,
        BUSINESS_OPERATOR_COMMENT);

    // when
    changeRequest.cancel(APPROVER_USERNAME, APPROVER_COMMENT);

    // then
    assertThat(changeRequest.getState()).isEqualTo(CANCELLED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(APPROVER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(APPROVER_COMMENT);
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }
}
