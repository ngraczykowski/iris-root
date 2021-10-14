package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.*;

import com.silenteight.payments.bridge.common.jpa.BaseVersionedEntity;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.NA;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AlertMessageStatus")
class AlertMessageStatusEntity extends BaseVersionedEntity {

  @Id
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private UUID alertMessageId;

  private OffsetDateTime storedAt;

  private OffsetDateTime acceptedAt;

  private OffsetDateTime recommendedAt;

  private OffsetDateTime rejectedAt;

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

  TransitionResult transitionStatus(AlertMessageStatus destinationStatus,
      DeliveryStatus deliveryStatus, Clock clock) {

    if (this.status == destinationStatus && this.deliveryStatus == deliveryStatus) {
      return TransitionResult.IGNORED;
    }

    if (destinationStatus.isFinal() == deliveryStatus.equals(NA)) {
      return TransitionResult.FAILED;
    }

    if (this.status.isFinal() && this.status == destinationStatus
        && this.deliveryStatus.isFinal()) {
      return deliveryStatus == PENDING ?
             TransitionResult.IGNORED : TransitionResult.FAILED;
    }

    if (this.status != destinationStatus &&
        !status.isTransitionAllowed(destinationStatus)) {
      return TransitionResult.FAILED;
    }

    this.status = destinationStatus;
    this.deliveryStatus = deliveryStatus;

    updateChangeTime(OffsetDateTime.now(clock));
    setCurrentTimeForUpdatedAt();
    return TransitionResult.SUCCESS;
  }

  void transitionStatusOrElseThrow(AlertMessageStatus destinationStatus,
      DeliveryStatus deliveryStatus, Clock clock) {
    if (transitionStatus(destinationStatus, deliveryStatus, clock) == TransitionResult.FAILED) {
      throw new IllegalStateException(
          "Unable to transition to status " + destinationStatus + ", from status " + status);
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

}
