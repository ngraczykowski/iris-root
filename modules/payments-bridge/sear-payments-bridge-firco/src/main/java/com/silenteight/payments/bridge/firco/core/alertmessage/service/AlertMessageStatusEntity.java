package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.*;

import com.silenteight.payments.bridge.common.jpa.BaseVersionedEntity;
import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_DAMAGED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;
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

  private static final AlertMessageStatus[] FINAL_STATUSES =
      { RECOMMENDED, REJECTED_OUTDATED, REJECTED_DAMAGED, REJECTED_OVERFLOWED };

  @Id
  @Column(updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private UUID alertMessageId;

  private OffsetDateTime storedAt;

  private OffsetDateTime acceptedAt;

  private OffsetDateTime recommendedAt;

  private OffsetDateTime rejectedAt;

  @Column(updatable = false, nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private AlertMessageStatus status;

  AlertMessageStatusEntity(UUID alertMessageId) {
    this.alertMessageId = alertMessageId;
    status = AlertMessageStatus.RECEIVED;
  }

  AlertMessageStatusEntity transitionStatus(AlertMessageStatus destinationStatus, Clock clock) {
    if (status == destinationStatus) {
      return this;
    }

    if (Arrays.binarySearch(FINAL_STATUSES, status) < 0) {
      throw new IllegalStateException(
          "Unable to transition to status " + destinationStatus + ", from final status " + status);
    }

    switch (destinationStatus) {
      case STORED:
        storedAt = OffsetDateTime.now(clock);
        break;
      case ACCEPTED:
        acceptedAt = OffsetDateTime.now(clock);
        break;
      case RECOMMENDED:
        recommendedAt = OffsetDateTime.now(clock);
        break;
      case REJECTED_OUTDATED:
      case REJECTED_DAMAGED:
      case REJECTED_OVERFLOWED:
        rejectedAt = OffsetDateTime.now(clock);
        break;
      case RECEIVED:
      default:
        throw new IllegalStateException("Invalid destination status: " + destinationStatus);
    }

    setCurrentTimeForUpdatedAt();

    status = destinationStatus;

    return this;
  }
}
