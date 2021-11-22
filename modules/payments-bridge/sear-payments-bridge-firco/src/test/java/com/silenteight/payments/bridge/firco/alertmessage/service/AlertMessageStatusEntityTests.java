package com.silenteight.payments.bridge.firco.alertmessage.service;


import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_DAMAGED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.STORED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.DELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.NA;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.FAILED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.IGNORED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AlertMessageStatusEntityTests {

  Clock clock = Clock.systemUTC();

  @Test
  void shouldTransferToRecommendedDelivered() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, PENDING, clock));
    var recommendedAt = status.getRecommendedAt();
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, DELIVERED, clock));
    assertNotNull(status.getDeliveredAt());
    assertEquals(recommendedAt, status.getRecommendedAt());
  }

  @Test
  void shouldIgnorePendingDelivery() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, DELIVERED, clock));
    assertEquals(IGNORED, status.transitionStatus(RECOMMENDED, PENDING, clock));
  }

  @Test
  void shouldFailedChangingFinalDeliveryStatus() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, DELIVERED, clock));
    assertEquals(FAILED, status.transitionStatus(RECOMMENDED, UNDELIVERED, clock));
  }

  @Test
  void shouldFailedChangingFinalStatus() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, DELIVERED, clock));
    assertEquals(FAILED, status.transitionStatus(REJECTED_OUTDATED, DELIVERED, clock));
  }

  @Test
  void shouldFailedChangingFinalStatus2() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(RECOMMENDED, DELIVERED, clock));
    assertEquals(FAILED, status.transitionStatus(REJECTED_OUTDATED, PENDING, clock));
  }

  @Test
  void shouldLeftDeliveryAtBlank() {
    var status = new AlertMessageStatusEntity(UUID.randomUUID());
    assertEquals(SUCCESS, status.transitionStatus(STORED, NA, clock));
    assertEquals(SUCCESS, status.transitionStatus(REJECTED_DAMAGED, PENDING, clock));
    var rejectedAt = status.getRejectedAt();
    assertEquals(SUCCESS, status.transitionStatus(REJECTED_DAMAGED, UNDELIVERED, clock));
    assertNull(status.getDeliveredAt());
    assertEquals(rejectedAt, status.getRejectedAt());
  }

}
