package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.*;

import com.silenteight.payments.bridge.common.jpa.BaseVersionedEntity;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

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
  private AlertMessageStatus status;

  AlertMessageStatusEntity(UUID alertMessageId) {
    this.alertMessageId = alertMessageId;
    this.status = AlertMessageStatus.RECEIVED;
  }

  boolean transitionStatus(AlertMessageStatus destinationStatus, Clock clock) {
    if (status == destinationStatus || status.isFinal()) {
      return false;
    }

    if (!status.isTransitionAllowed(destinationStatus)) {
      return false;
    }

    status = destinationStatus;
    updateChangeTime(OffsetDateTime.now(clock));
    setCurrentTimeForUpdatedAt();
    return true;
  }

  void transitionStatusOrElseThrow(AlertMessageStatus destinationStatus, Clock clock) {
    if (!transitionStatus(destinationStatus, clock)) {
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
