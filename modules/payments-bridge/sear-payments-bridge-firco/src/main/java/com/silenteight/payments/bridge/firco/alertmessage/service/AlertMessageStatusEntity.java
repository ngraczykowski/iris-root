package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.jpa.BaseVersionedEntity;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.DELIVERED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.NA;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.FAILED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.IGNORED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.TransitionResult.SUCCESS;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AlertMessageStatus")
@Slf4j
class AlertMessageStatusEntity extends BaseVersionedEntity {

  @Id
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private UUID alertMessageId;

  private OffsetDateTime storedAt;

  private OffsetDateTime acceptedAt;

  private OffsetDateTime recommendedAt;

  private OffsetDateTime rejectedAt;

  private OffsetDateTime deliveredAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private DeliveryStatus deliveryStatus;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private AlertMessageStatus status;

  AlertMessageStatusEntity(UUID alertMessageId) {
    this.alertMessageId = alertMessageId;
    this.status = AlertMessageStatus.RECEIVED;
    this.deliveryStatus = DeliveryStatus.NA;
  }

  TransitionResult transitionStatus(AlertMessageStatus targetStatus,
      DeliveryStatus targetDeliveryStatus, Clock clock) {

    var statusResult = verifyStatusUpdate(targetStatus);
    var deliveryResult = verifyDeliveryUpdate(targetStatus, targetDeliveryStatus);

    var result = Collections.max(List.of(statusResult, deliveryResult));
    if (result == SUCCESS) {
      updateStatus(targetStatus, clock);
      updateDelivery(targetDeliveryStatus, clock);
      setCurrentTimeForUpdatedAt();
    }

    return result;
  }

  private TransitionResult verifyStatusUpdate(AlertMessageStatus targetStatus) {
    if (status == targetStatus) {
      return IGNORED;
    }

    if (!status.isTransitionAllowed(targetStatus)) {
      log.debug("Unable to transition to status " + targetStatus + " from status " + status);
      return FAILED;
    }

    return SUCCESS;
  }

  private TransitionResult verifyDeliveryUpdate(AlertMessageStatus targetStatus,
      DeliveryStatus targetDeliveryStatus) {
    if (deliveryStatus == targetDeliveryStatus) {
      return IGNORED;
    }

    if (targetStatus.isFinal() == targetDeliveryStatus.equals(NA)) {
      log.error("DeliveryStatus NA is only applicable to the final state");
      return FAILED;
    }

    if (status.isFinal() && status == targetStatus
        && deliveryStatus.isFinal()) {
      return targetDeliveryStatus == PENDING ? IGNORED : FAILED;
    }
    return SUCCESS;
  }

  private void updateStatus(AlertMessageStatus targetStatus, Clock clock) {
    if (status != targetStatus) {
      status = targetStatus;
      updateChangeTime(OffsetDateTime.now(clock));
    }
  }

  private void updateChangeTime(OffsetDateTime dateTime) {
    switch (status) {
      case STORED:
        storedAt = dateTime;
        break;
      case ACCEPTED:
        acceptedAt = dateTime;
        break;
      case RECOMMENDED:
        recommendedAt = dateTime;
        break;
      case REJECTED_OUTDATED:
      case REJECTED_DAMAGED:
      case REJECTED_OVERFLOWED:
        rejectedAt = dateTime;
        break;
      default:
    }
  }

  private void updateDelivery(DeliveryStatus targetDeliveryStatus, Clock clock) {
    if (deliveryStatus != targetDeliveryStatus) {
      deliveryStatus = targetDeliveryStatus;
      if (DELIVERED == targetDeliveryStatus) {
        deliveredAt = OffsetDateTime.now(clock);
      }
    }
  }

}
