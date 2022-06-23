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
  private static final String MODEL_TUNER_USERNAME = "MODEL_TUNER";
  private static final String MODEL_TUNER_COMMENT = "This is comment from Model Tuner";
  private static final String DECIDER_USERNAME = "decider";
  private static final String DECIDER_COMMENT = "decider comment 1234";

  @Test
  void changeRequestApproved_stateIsChangedToApproved() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        MODEL_TUNER_USERNAME,
        MODEL_TUNER_COMMENT);

    // when
    changeRequest.approve(DECIDER_USERNAME, DECIDER_COMMENT);

    // then
    assertThat(changeRequest.getState()).isEqualTo(APPROVED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(DECIDER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(DECIDER_COMMENT);
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }

  @Test
  void changeRequestRejected_stateIsChangedToRejected() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        MODEL_TUNER_USERNAME,
        MODEL_TUNER_COMMENT);

    // when
    changeRequest.reject(DECIDER_USERNAME, DECIDER_COMMENT);

    // then
    assertThat(changeRequest.getState()).isEqualTo(REJECTED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(DECIDER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isEqualTo(DECIDER_COMMENT);
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }

  @Test
  void changeRequestCancelled_stateIsChangedToCancelled() {
    // give
    OffsetDateTime now = now();
    ChangeRequest changeRequest = new ChangeRequest(
        CHANGE_CHANGE_ID,
        MODEL_NAME,
        MODEL_TUNER_USERNAME,
        MODEL_TUNER_COMMENT);

    // when
    changeRequest.cancel(DECIDER_USERNAME);

    // then
    assertThat(changeRequest.getState()).isEqualTo(CANCELLED);
    assertThat(changeRequest.getDecidedBy()).isEqualTo(DECIDER_USERNAME);
    assertThat(changeRequest.getDeciderComment()).isNull();
    assertThat(changeRequest.getDecidedAt()).isAfter(now);
  }
}
